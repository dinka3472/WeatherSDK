package com.weather;

import com.weather.apiClient.DefaultWeatherApiClient;
import com.weather.apiClient.WeatherApiClient;
import com.weather.cache.CacheWrapper;
import com.weather.cache.GuavaCacheWrapper;
import com.weather.exception.WeatherApiException;
import com.weather.http.DefaultHttpClientWrapper;
import com.weather.http.HttpClientWrapper;
import com.weather.model.WeatherResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The {@code WeatherSdk} provides an interface for retrieving weather data from the OpenWeather API.
 * It supports both on-demand and polling modes, allowing users to fetch real-time weather data
 * or automatically refresh it at specified intervals.
 * <p>
 * The SDK ensures that only one instance per API key can exist at a time, preventing duplicate
 * configurations and ensuring efficient resource management.
 * <p>
 * Additionally, users can customize caching settings by providing their own cache implementation
 * via {@link CacheWrapper} or configuring cache expiration and size limits.
 * <p>
 * Custom HTTP client implementations can also be set through {@link HttpClientWrapper}, allowing
 * full control over network requests.
 */
public class WeatherSdk {
    private static final Map<String, WeatherSdk> sdkMap = new HashMap<>();
    private final String apiKey;
    private final boolean pollingMode;
    private final int pollingIntervalMinutes;
    private final int cacheValidityMinutes;
    private final int maxCacheSize;
    private final CacheWrapper<String, WeatherResponse> cache;
    private final HttpClientWrapper httpClientWrapper;
    private final WeatherApiClient weatherApiClient;
    private PollingService pollingService;

    private WeatherSdk(Builder builder) {
        this.apiKey = builder.apiKey;
        this.pollingMode = builder.pollingMode;
        this.pollingIntervalMinutes = builder.pollingIntervalMinutes;
        this.httpClientWrapper = builder.httpClientWrapper;
        this.cacheValidityMinutes = builder.cacheValidityMinutes;
        this.maxCacheSize = builder.maxCacheSize;
        this.cache = (builder.cache != null)
                ? builder.cache
                : new GuavaCacheWrapper<>(cacheValidityMinutes, TimeUnit.MINUTES, maxCacheSize);

        this.weatherApiClient = new DefaultWeatherApiClient(apiKey, httpClientWrapper);
        this.pollingService = pollingMode ? new PollingService(cache, weatherApiClient, pollingIntervalMinutes) : null;
        if (pollingService != null) {
            pollingService.startPolling();
        }
    }

    /**
     * Creates a new builder instance for constructing a {@link WeatherSdk} object.
     *
     * @return a new instance of {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Retrieves weather data for a specified city.
     * If the data is available in the cache, it is returned immediately.
     * Otherwise, a request is made to the OpenWeather API.
     *
     * @param cityName the name of the city
     * @return a {@link WeatherResponse} containing the weather data
     * @throws WeatherApiException if the API request fails
     */
    public WeatherResponse getWeather(String cityName) throws WeatherApiException {
        try {
            String key = cityName.toLowerCase();
            WeatherResponse cachedData = cache.get(key);
            if (cachedData != null) {
                return cachedData;
            }
            WeatherResponse response = weatherApiClient.getWeatherByCity(cityName);
            cache.put(key, response);
            return response;
        } catch (WeatherApiException e) {
            throw e;
        } catch (Exception ex) {
            throw new WeatherApiException(ex.getMessage());
        }
    }

    /**
     * Removes the current instance of {@link WeatherSdk} from the global SDK map.
     * Stops the polling service (if active) and invalidates the cache.
     */
    public void remove() {
        if (pollingService != null) {
            pollingService.stopPolling();
        }
        cache.invalidateCache();
        sdkMap.remove(apiKey);
    }

    public String getApiKey() {
        return apiKey;
    }

    public boolean isPollingMode() {
        return pollingMode;
    }

    public int getPollingIntervalMinutes() {
        return pollingIntervalMinutes;
    }

    public int getCacheValidityMinutes() {
        return cacheValidityMinutes;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    /**
     * Builder class for constructing instances of {@link WeatherSdk}.
     */
    public static class Builder {
        private static final boolean DEFAULT_POLLING_MODE = false;
        private static final int DEFAULT_POLLING_INTERVAL_MINUTES = 10;
        private static final int DEFAULT_CACHE_VALIDITY_MINUTES = 10;
        private static final int DEFAULT_MAX_CACHE_SIZE = 10;
        private String apiKey;
        private boolean pollingMode = DEFAULT_POLLING_MODE;
        private int pollingIntervalMinutes = DEFAULT_POLLING_INTERVAL_MINUTES;
        private int cacheValidityMinutes = DEFAULT_CACHE_VALIDITY_MINUTES;
        private int maxCacheSize = DEFAULT_MAX_CACHE_SIZE;
        private HttpClientWrapper httpClientWrapper = new DefaultHttpClientWrapper();
        private CacheWrapper<String, WeatherResponse> cache = null;

        /**
         * Sets the API key for accessing the OpenWeather API.
         * This is a required parameter.
         *
         * @param apiKey the API key used for authentication
         * @return the builder instance for method chaining
         */
        public Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Enables or disables polling mode.
         * When enabled, the SDK will automatically fetch updated weather data at regular intervals.
         *
         * @param pollingMode {@code true} to enable polling mode, {@code false} to disable it
         * @return the builder instance for method chaining
         */
        public Builder setPollingMode(boolean pollingMode) {
            this.pollingMode = pollingMode;
            return this;
        }

        /**
         * Sets the polling interval in minutes.
         * This determines how often the weather data will be updated when polling mode is enabled.
         *
         * @param minutes the polling interval in minutes
         * @return the builder instance for method chaining
         */
        public Builder setPollingIntervalMinutes(int minutes) {
            this.pollingIntervalMinutes = minutes;
            return this;
        }

        /**
         * Sets the cache validity duration in minutes.
         * Cached weather data will be considered valid for this duration before being refreshed.
         *
         * @param minutes the duration in minutes for which cached data remains valid
         * @return the builder instance for method chaining
         */
        public Builder setCacheValidityMinutes(int minutes) {
            this.cacheValidityMinutes = minutes;
            return this;
        }

        /**
         * Sets the maximum cache size.
         * Determines the maximum number of cities that can be stored in the cache at a time.
         *
         * @param size the maximum number of cached entries
         * @return the builder instance for method chaining
         */
        public Builder setMaxCacheSize(int size) {
            this.maxCacheSize = size;
            return this;
        }

        /**
         * Sets a custom {@link HttpClientWrapper} implementation.
         * This allows the user to provide a custom HTTP client for making API requests.
         * If not set, the default implementation will be used.
         *
         * @param httpClient the custom {@link HttpClientWrapper} instance
         * @return the builder instance for method chaining
         */
        public Builder setHttpClientWrapper(HttpClientWrapper httpClient) {
            this.httpClientWrapper = httpClient;
            return this;
        }

        /**
         * Sets a custom cache implementation.
         * This allows the user to provide their own caching mechanism instead of the default Guava cache.
         *
         * @param cache a custom implementation of {@link CacheWrapper}
         * @return the builder instance for method chaining
         */
        public Builder setCache(CacheWrapper<String, WeatherResponse> cache) {
            this.cache = cache;
            return this;
        }

        /**
         * Builds and returns an instance of {@link WeatherSdk}.
         * Ensures that an SDK with the same API key does not already exist.
         *
         * @return a new instance of {@link WeatherSdk}
         * @throws IllegalArgumentException if the API key is null or empty
         * @throws IllegalStateException    if an SDK instance with the same API key already exists
         */
        public WeatherSdk build() {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new IllegalArgumentException("API key is required and cannot be null or empty.");
            }
            if (sdkMap.containsKey(apiKey)) {
                throw new IllegalStateException(
                        String.format("An instance of WeatherSdk with API key '%s' already exists. ", apiKey));
            }
            WeatherSdk weatherSdk = new WeatherSdk(this);
            sdkMap.put(apiKey, weatherSdk);
            return weatherSdk;
        }
    }
}

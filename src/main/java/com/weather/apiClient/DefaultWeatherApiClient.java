package com.weather.apiClient;

import com.weather.exception.CityNotFoundException;
import com.weather.http.HttpClientWrapper;
import com.weather.model.CityGeoResponse;
import com.weather.model.WeatherApiResponse;
import com.weather.model.WeatherResponse;
import com.weather.model.WeatherResponseConverter;

public class DefaultWeatherApiClient implements WeatherApiClient {
    private static final String GEO_URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s";

    private final HttpClientWrapper httpClientWrapper;
    private final String apiKey;

    public DefaultWeatherApiClient(String apiKey, HttpClientWrapper httpClientWrapper) {
        this.apiKey = apiKey;
        this.httpClientWrapper = httpClientWrapper;
    }

    /**
     * Retrieves the current weather data for a given city.
     * <p>
     * This method first fetches the geographic coordinates of the city and then
     * retrieves weather information based on those coordinates.
     * </p>
     *
     * @param cityName the name of the city
     * @return a {@link WeatherResponse} object containing weather details
     * @throws Exception if the request fails or the city is not found
     */
    @Override
    public WeatherResponse getWeatherByCity(String cityName) throws Exception {
        CityGeoResponse cityGeo = getCityGeo(cityName);
        String weatherUrl = String.format(WEATHER_URL, cityGeo.getLat(), cityGeo.getLon(), apiKey);
        WeatherApiResponse weatherApiResponse = httpClientWrapper.get(weatherUrl, WeatherApiResponse.class);
        return WeatherResponseConverter.convert(weatherApiResponse);
    }

    /**
     * Retrieves the geographic coordinates of a given city.
     *
     * @param cityName the name of the city
     * @return a {@link CityGeoResponse} object containing latitude and longitude
     * @throws Exception if the city is not found or the request fails
     */
    private CityGeoResponse getCityGeo(String cityName) throws Exception {
        String geoUrl = String.format(GEO_URL, cityName, apiKey);
        CityGeoResponse[] cityGeoArray = httpClientWrapper.get(geoUrl, CityGeoResponse[].class);
        if (cityGeoArray.length == 0) {
            throw new CityNotFoundException("City not found: " + cityName);
        }
        return cityGeoArray[0];
    }
}

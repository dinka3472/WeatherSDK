package com.weather;

import com.weather.apiClient.WeatherApiClient;
import com.weather.cache.CacheWrapper;
import com.weather.model.WeatherResponse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PollingService {
    private final CacheWrapper<String, WeatherResponse> cache;
    private final WeatherApiClient weatherApiClient;
    private final ScheduledExecutorService executorService;
    private volatile boolean running = true;
    private final int pollingInterval;


    public PollingService(CacheWrapper<String, WeatherResponse> cache, WeatherApiClient weatherApiClient, int pollingInterval) {
        this.cache = cache;
        this.weatherApiClient = weatherApiClient;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.pollingInterval = pollingInterval;
    }

    public void startPolling() {
        executorService.scheduleAtFixedRate(this::pollWeatherData, pollingInterval, pollingInterval, TimeUnit.MINUTES);
    }

    private void pollWeatherData() {
        if (!running) {
            return;
        }
        try {
            Iterable<String> cities = cache.getAllKeys();
            for (String city : cities) {
                try {
                    WeatherResponse weather = weatherApiClient.getWeatherByCity(city);
                    cache.put(city.toLowerCase(), weather);
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void stopPolling() {
        running = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

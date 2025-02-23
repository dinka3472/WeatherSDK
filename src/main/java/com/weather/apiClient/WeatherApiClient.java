package com.weather.apiClient;


import com.weather.model.WeatherResponse;

public interface WeatherApiClient {
    WeatherResponse getWeatherByCity(String cityName) throws Exception;
}

package com.weather.exception;

public class CityNotFoundException extends WeatherApiException {
    public CityNotFoundException(String message) {
        super(message);
    }
}

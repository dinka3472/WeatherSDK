package com.weather.exception;

public class TokenInvalidException extends WeatherApiException {
    public TokenInvalidException(String message) {
        super(message);
    }
}

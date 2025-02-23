package com.weather.model;

import java.util.List;

public final class WeatherResponseConverter {
    /**
     * Converts a full WeatherResponse from OpenWeather API to a simplified WeatherResponse DTO.
     *
     * @param weatherApiResponse the full API response
     * @return a simplified WeatherResponse DTO
     */
    public static WeatherResponse convert(WeatherApiResponse weatherApiResponse) {
        WeatherResponse weatherResponse = new WeatherResponse();

        if (weatherApiResponse.getWeather() != null && !weatherApiResponse.getWeather().isEmpty()) {
            WeatherResponse.WeatherInfo weatherInfo = new WeatherResponse.WeatherInfo();
            weatherInfo.setMain(weatherApiResponse.getWeather().get(0).getMain());
            weatherInfo.setDescription(weatherApiResponse.getWeather().get(0).getDescription());
            weatherResponse.setWeather(List.of(weatherInfo));
        }

        if (weatherApiResponse.getMain() != null) {
            WeatherResponse.TemperatureInfo temperatureInfo = new WeatherResponse.TemperatureInfo();
            temperatureInfo.setTemp(weatherApiResponse.getMain().getTemp());
            temperatureInfo.setFeelsLike(weatherApiResponse.getMain().getFeelsLike());
            weatherResponse.setTemperature(temperatureInfo);
        }

        if (weatherApiResponse.getWind() != null) {
            WeatherResponse.WindInfo windInfo = new WeatherResponse.WindInfo();
            windInfo.setSpeed(weatherApiResponse.getWind().getSpeed());
            weatherResponse.setWind(windInfo);
        }

        if (weatherApiResponse.getSys() != null) {
            WeatherResponse.SysInfo sysInfo = new WeatherResponse.SysInfo();
            sysInfo.setSunrise(weatherApiResponse.getSys().getSunrise());
            sysInfo.setSunset(weatherApiResponse.getSys().getSunset());
            weatherResponse.setSys(sysInfo);
        }

        weatherResponse.setVisibility(weatherApiResponse.getVisibility());
        weatherResponse.setDatetime(weatherApiResponse.getDt());
        weatherResponse.setTimezone(weatherApiResponse.getTimezone());
        weatherResponse.setName(weatherApiResponse.getName());

        return weatherResponse;
    }
}

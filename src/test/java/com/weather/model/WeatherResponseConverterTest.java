package com.weather.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WeatherResponseConverterTest {

    @Test
    void convert() {
        // Given
        WeatherApiResponse weatherApiResponse = new WeatherApiResponse();

        WeatherApiResponse.Weather weather = new WeatherApiResponse.Weather();
        weather.setMain("Rain");
        weather.setDescription("moderate rain");
        weatherApiResponse.setWeather(List.of(weather));

        WeatherApiResponse.Main main = new WeatherApiResponse.Main();
        main.setTemp(285.2);
        main.setFeelsLike(283.1);
        weatherApiResponse.setMain(main);

        WeatherApiResponse.Wind wind = new WeatherApiResponse.Wind();
        wind.setSpeed(3.5);
        weatherApiResponse.setWind(wind);

        WeatherApiResponse.Sys sys = new WeatherApiResponse.Sys();
        sys.setSunrise(1740278833);
        sys.setSunset(1740315333);
        weatherApiResponse.setSys(sys);

        weatherApiResponse.setVisibility(10000);
        weatherApiResponse.setDt(1620010000);
        weatherApiResponse.setTimezone(3600);
        weatherApiResponse.setName("Test City");

        // When
        WeatherResponse weatherResponse = WeatherResponseConverter.convert(weatherApiResponse);

        // Then
        assertNotNull(weatherResponse);
        assertNotNull(weatherResponse.getWeather());
        assertEquals(1, weatherResponse.getWeather().size());
        assertEquals("Rain", weatherResponse.getWeather().get(0).getMain());
        assertEquals("moderate rain", weatherResponse.getWeather().get(0).getDescription());

        assertNotNull(weatherResponse.getTemperature());
        assertEquals(285.2, weatherResponse.getTemperature().getTemp());
        assertEquals(283.1, weatherResponse.getTemperature().getFeelsLike());

        assertNotNull(weatherResponse.getWind());
        assertEquals(3.5, weatherResponse.getWind().getSpeed());

        assertNotNull(weatherResponse.getSys());
        assertEquals(1740278833, weatherResponse.getSys().getSunrise());
        assertEquals(1740315333, weatherResponse.getSys().getSunset());

        assertEquals(10000, weatherResponse.getVisibility());
        assertEquals(1620010000, weatherResponse.getDatetime());
        assertEquals(3600, weatherResponse.getTimezone());
        assertEquals("Test City", weatherResponse.getName());
    }
}
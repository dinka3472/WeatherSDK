package com.weather;

import com.weather.model.WeatherResponse;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String apiKey = "e4b221cd3dfa30b3d672b645016e8f05";
        WeatherSdk weatherSdk = WeatherSdk
                .builder()
                .setApiKey(apiKey)
                .setPollingMode(true)
                .build();

        WeatherResponse ufa = weatherSdk.getWeather("Ufa");
        System.out.println(ufa.toString());
        WeatherResponse tumen = weatherSdk.getWeather("Tumen");
        System.out.println(tumen.toString());
        Thread.sleep(660000);
        weatherSdk.remove();
    }
}
package com.weather.model;

import java.util.List;

public class WeatherResponse {

    private List<WeatherInfo> weather;
    private TemperatureInfo temperature;
    private int visibility;
    private WindInfo wind;
    private long datetime;
    private SysInfo sys;
    private int timezone;
    private String name;

    public WeatherResponse() {
    }

    public List<WeatherInfo> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherInfo> weather) {
        this.weather = weather;
    }

    public TemperatureInfo getTemperature() {
        return temperature;
    }

    public void setTemperature(TemperatureInfo temperature) {
        this.temperature = temperature;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public WindInfo getWind() {
        return wind;
    }

    public void setWind(WindInfo wind) {
        this.wind = wind;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public SysInfo getSys() {
        return sys;
    }

    public void setSys(SysInfo sys) {
        this.sys = sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class WeatherInfo {
        private String main;
        private String description;

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "WeatherInfo{" +
                    "main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public static class TemperatureInfo {
        private double temp;
        private double feelsLike;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(double feelsLike) {
            this.feelsLike = feelsLike;
        }

        @Override
        public String toString() {
            return "TemperatureInfo{" +
                    "temp=" + temp +
                    ", feelsLike=" + feelsLike +
                    '}';
        }
    }

    public static class WindInfo {
        private double speed;

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        @Override
        public String toString() {
            return "WindInfo{" +
                    "speed=" + speed +
                    '}';
        }
    }

    public static class SysInfo {
        private long sunrise;
        private long sunset;

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }

        @Override
        public String toString() {
            return "SysInfo{" +
                    "sunrise=" + sunrise +
                    ", sunset=" + sunset +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WeatherResultDto{" +
                "weather=" + weather +
                ", temperature=" + temperature +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", datetime=" + datetime +
                ", sys=" + sys +
                ", timezone=" + timezone +
                ", name='" + name + '\'' +
                '}';
    }
}

# Weather SDK

## Overview
The `WeatherSdk` allows you to fetch real-time weather data for a given city using the  **OpenWeather API** https://openweathermap.org/.
It supports both **on-demand mode** (fetching real-time weather data) and **polling mode** (automatic data refresh at specified intervals).

The SDK returns information about the first city which was found by searching for the city
name.

Additionally, users can:
- Configure caching settings using a custom cache implementation or the built-in **Guava Cache**.
- Use a **custom HTTP client** for API requests.
- Ensure that only **one instance per API key** exists at a time.

## Features
✅ Fetch real-time weather data by city name  
✅ Configurable **polling mode** for automatic data refresh  
✅ **Customizable cache** settings with expiration and size limits  
✅ Supports custom **HTTP client implementations**  


## Installation

### Using Maven
1. Build project 
```
mvn clean package
```
2. Add dependency to your local repository
```
mvn install:install-file -Dfile="target/WeatherSDK-1.0-SNAPSHOT.jar" -DgroupId="com.weather" -DartifactId=WeatherSDK -Dversion="1.0-SNAPSHOT" -Dpackaging=jar
```
3. Add the following dependency to your project `pom.xml`:
```xml
<dependency>
    <groupId>com.weather</groupId>
    <artifactId>WeatherSDK</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```


## Usage
To work with the SDK, you need to get an api key. You can get your api key here https://openweathermap.org/appid.  This is the minimum required configuration for the operation of the SDK.
If you only set the api key, the following configurations will be applied by default:
- polling mode = false;
- polling interval minutes = 5;
- cache validity minutes = 10;
- cache size = 10;


### 1. Basic Example (On-Demand Mode):

```java
WeatherSdk weatherSdk = WeatherSdk.builder()
        .setApiKey("your-api-key")  // Required
        .build();
WeatherResponse response = weatherSdk.getWeather("New York");
```

### 2. Using Polling Mode

```java
WeatherSdk weatherSdk = WeatherSdk.builder()
        .setApiKey("your-api-key")
        .setPollingMode(true)  // Enable polling mode
        .setPollingIntervalMinutes(10)  // Refresh data every 10 minutes
        .build();

```
### 3. Customizing Cache Settings

```java
WeatherSdk weatherSdk = WeatherSdk.builder()
        .setApiKey("your-api-key")
        .setCacheValidityMinutes(30)  // Cache expires after 30 minutes
        .setMaxCacheSize(20)  // Store up to 20 city weather records
        .build();

```
### 4. Custom Cache Implementation
By default, the SDK uses Guava Cache for storing weather data. However, you can provide your own cache implementation by implementing the CacheWrapper<K, V> interface and passing it to the SDK builder.
```java
public class MyCustomCache implements CacheWrapper<String, WeatherResponse> {
    private final ConcurrentHashMap<String, WeatherResponse> cache = new ConcurrentHashMap<>();

    @Override
    public WeatherResponse get(String key) {
        return cache.get(key);
    }

    @Override
    public void put(String key, WeatherResponse value) {
        cache.put(key, value);
        
        //.... other method override
    }
}

CacheWrapper<String, WeatherResponse> myCache = new MyCustomCache<>();

WeatherSdk weatherSdk = WeatherSdk.builder()
        .setApiKey("your-api-key")
        .setCache(myCache)  // Use custom cache implementation
        .build();
```

## Exception Handling
The SDK throws the following exceptions:

| Exception                | Description                                  |
|--------------------------|----------------------------------------------|
| `CityNotFoundException`  | Thrown when the specified city is not found. |
| `TokenInvalidException`  | Thrown when the API key is invalid.          |
| `WeatherApiException`    | Common exception for API request failures.   |

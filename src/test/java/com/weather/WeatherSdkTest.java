package com.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WeatherSdkTest {

    private static final String TEST_API_KEY = "test-api-key";

    private WeatherSdk weatherSdk;

    @BeforeEach
    void setUp() {
        weatherSdk = WeatherSdk.builder()
                .setApiKey(TEST_API_KEY) // Передаем только API-ключ
                .build();
    }

    @Test
    public void testDefaultSettings_AreSet_WhenOnlyApiKeyProvided() {
        assertEquals(TEST_API_KEY, weatherSdk.getApiKey());
        assertEquals(10, weatherSdk.getMaxCacheSize());
        assertEquals(10, weatherSdk.getCacheValidityMinutes());
        assertFalse(weatherSdk.isPollingMode(), "Polling mode should be false by default");
        assertEquals(10, weatherSdk.getPollingIntervalMinutes(), "Default polling interval should be 10 minutes");

    }
}
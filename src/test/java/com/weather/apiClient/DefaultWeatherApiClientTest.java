package com.weather.apiClient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.weather.exception.TokenInvalidException;
import com.weather.exception.WeatherApiException;
import com.weather.http.DefaultHttpClientWrapper;
import com.weather.model.WeatherApiResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultWeatherApiClientTest {

    private WireMockServer wireMockServer;
    private DefaultHttpClientWrapper httpClientWrapper;

    @BeforeAll
    void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        httpClientWrapper = new DefaultHttpClientWrapper();
    }

    @AfterAll
    void serverStop() {
        wireMockServer.stop();
    }

    @Test
    public void testGet_Success() throws IOException, InterruptedException {
        // Given
        String jsonResponse = getTestJsonResponse();
        stubFor(get(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)));

        // When
        WeatherApiResponse response = httpClientWrapper.get(wireMockServer.baseUrl() + "/test", WeatherApiResponse.class);

        // Then
        assertNotNull(response);
        assertEquals("Province of Turin", response.getName());
        assertEquals(284.2, response.getMain().getTemp());
    }


    @Test
    public void testGet_InvalidApiKey_ThrowsTokenInvalidException() {
        // Given
        stubFor(get(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(401)));

        // When
        assertThrows(TokenInvalidException.class, () -> {
            httpClientWrapper.get(wireMockServer.baseUrl() + "/test", WeatherApiResponse.class);
        });
    }

    @Test
    public void testGet_ApiError_ThrowsWeatherApiException() {
        // Given
        stubFor(get(urlEqualTo("/test"))
                .willReturn(aResponse()
                        .withStatus(500)));

        // When
        Exception exception = assertThrows(WeatherApiException.class, () -> {
            httpClientWrapper.get(wireMockServer.baseUrl() + "/test", WeatherApiResponse.class);
        });

        assertTrue(exception.getMessage().contains("API request failed with status: 500"));
    }

    private String getTestJsonResponse() {
        return "{\n" +
                "   \"coord\": {\n" +
                "      \"lon\": 7.367,\n" +
                "      \"lat\": 45.133\n" +
                "   },\n" +
                "   \"weather\": [\n" +
                "      {\n" +
                "         \"id\": 501,\n" +
                "         \"main\": \"Rain\",\n" +
                "         \"description\": \"moderate rain\",\n" +
                "         \"icon\": \"10d\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"base\": \"stations\",\n" +
                "   \"main\": {\n" +
                "      \"temp\": 284.2,\n" +
                "      \"feels_like\": 282.93,\n" +
                "      \"temp_min\": 283.06,\n" +
                "      \"temp_max\": 286.82,\n" +
                "      \"pressure\": 1021,\n" +
                "      \"humidity\": 60,\n" +
                "      \"sea_level\": 1021,\n" +
                "      \"grnd_level\": 910\n" +
                "   },\n" +
                "   \"visibility\": 10000,\n" +
                "   \"wind\": {\n" +
                "      \"speed\": 4.09,\n" +
                "      \"deg\": 121,\n" +
                "      \"gust\": 3.47\n" +
                "   },\n" +
                "   \"rain\": {\n" +
                "      \"1h\": 2.73\n" +
                "   },\n" +
                "   \"clouds\": {\n" +
                "      \"all\": 83\n" +
                "   },\n" +
                "   \"dt\": 1726660758,\n" +
                "   \"sys\": {\n" +
                "      \"type\": 1,\n" +
                "      \"id\": 6736,\n" +
                "      \"country\": \"IT\",\n" +
                "      \"sunrise\": 1726636384,\n" +
                "      \"sunset\": 1726680975\n" +
                "   },\n" +
                "   \"timezone\": 7200,\n" +
                "   \"id\": 3165523,\n" +
                "   \"name\": \"Province of Turin\",\n" +
                "   \"cod\": 200\n" +
                "}                    ";
    }
}
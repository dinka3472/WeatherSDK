package com.weather.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.exception.TokenInvalidException;
import com.weather.exception.WeatherApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/**
 * The {@code DefaultHttpClientWrapper} provides a default implementation of {@link HttpClientWrapper}
 * for making HTTP requests using Java's built-in {@link HttpClient}.
 * <p>
 * This class handles GET requests and automatically processes API responses, converting them into
 * the specified response type using Jackson's {@link ObjectMapper}.
 * </p>
 * <p>
 * It also includes built-in error handling for common API response statuses, throwing appropriate
 * exceptions such as {@link TokenInvalidException} and {@link WeatherApiException}.
 * </p>
 */
public class DefaultHttpClientWrapper implements HttpClientWrapper {
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    /**
     * Creates a new instance of {@code DefaultHttpClientWrapper} with a default HTTP client
     * and an instance of {@link ObjectMapper} for JSON parsing.
     */
    public DefaultHttpClientWrapper() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Sends a GET request to the specified URL and deserializes the response into the provided response type.
     *
     * @param url          the URL to send the GET request to
     * @param responseType the class type to deserialize the response into
     * @param <T>          the expected response type
     * @return the deserialized response object
     * @throws IOException          if an I/O error occurs when sending or receiving the request
     * @throws InterruptedException if the operation is interrupted
     * @throws TokenInvalidException if the API key is invalid (HTTP 401)
     * @throws WeatherApiException   if the API request fails with any other unexpected status code
     */
    @Override
    public <T> T get(String url, Class<T> responseType) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        processException(response);
        return objectMapper.readValue(response.body(), responseType);
    }

    private void processException(HttpResponse<String> response) {
        if (response.statusCode() == 401) {
            throw new TokenInvalidException("InvalidKey");
        }
        if (response.statusCode() != 200) {
            throw new WeatherApiException("API request failed with status: " + response.statusCode());
        }
    }
}

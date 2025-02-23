package com.weather.http;

/**
 * The {@code HttpClientWrapper} interface defines a contract for making HTTP GET requests.
 * <p>
 * Implementations of this interface should handle HTTP requests, process responses,
 * and convert them into the specified response type.
 * </p>
 * <p>
 * This abstraction allows for different HTTP client implementations to be used within the SDK,
 * making it flexible and customizable.
 * </p>
 */
public interface HttpClientWrapper {
    /**
     * Sends an HTTP GET request to the specified URL and deserializes the response into the given response type.
     *
     * @param url          the target URL for the GET request
     * @param responseType the class type to deserialize the response into
     * @param <T>          the expected response type
     * @return the deserialized response object of type {@code T}
     * @throws Exception if an error occurs during the request or response processing
     */
    <T> T get(String url, Class<T> responseType) throws Exception;
}

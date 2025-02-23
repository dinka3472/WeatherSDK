package com.weather.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * A wrapper around Guava Cache, providing basic caching operations.
 * <p>
 * This implementation supports expiration after write and a maximum cache size limit.
 * </p>
 *
 * @param <K> the type of cache keys
 * @param <V> the type of cache values
 */
public class GuavaCacheWrapper<K, V> implements CacheWrapper<K, V> {
    private final Cache<K, V> cache;

    /**
     * Constructs a Guava-based cache with the specified expiration time and size limit.
     *
     * @param expirationTime    the expirationTime after which cache entries expire
     * @param timeUnit    the time unit for the expiration expirationTime
     * @param maximumSize the maximum number of items allowed in the cache
     */
    public GuavaCacheWrapper(long expirationTime, TimeUnit timeUnit, long maximumSize) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expirationTime, timeUnit)
                .maximumSize(maximumSize)
                .build();
    }

    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void invalidate(K key) {
        cache.invalidate(key);
    }

    @Override
    public void invalidateCache() {
        cache.invalidateAll();
        cache.cleanUp();
    }

    @Override
    public Iterable<K> getAllKeys() {
        return cache.asMap().keySet();
    }
}
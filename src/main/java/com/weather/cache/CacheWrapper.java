package com.weather.cache;

/**
 * A generic cache wrapper interface for storing and retrieving cached data.
 * <p>
 * This interface provides basic operations for caching, including fetching, storing,
 * invalidating individual entries, and clearing the entire cache.
 * </p>
 *
 * @param <K> the type of keys used for cache entries
 * @param <V> the type of values stored in the cache
 */
public interface CacheWrapper<K, V> {
    /**
     * Retrieves a value from the cache by key.
     *
     * @param key the cache key
     * @return the cached value, or {@code null} if not found
     */
    V get(K key);

    /**
     * Stores a key-value pair in the cache.
     *
     * @param key   the cache key
     * @param value the value to store
     */
    void put(K key, V value);

    /**
     * Removes a value from the cache by key.
     *
     * @param key the cache key to remove
     */
    void invalidate(K key);

    /**
     * Clears all entries from the cache.
     */
    void invalidateCache();

    /**
     * Retrieves all keys currently stored in the cache.
     *
     * @return an iterable collection of cache keys
     */
    Iterable<K> getAllKeys();
}

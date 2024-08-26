package com.kir138.task1.model;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomCacheManager {
    private static final CustomCacheManager INSTANCE = new CustomCacheManager();
    private final Map<Long, City> cache;

    public CustomCacheManager() {
        this.cache = new HashMap<>();
    }

    public void putCity(Long id, City city) {
        cache.put(id, city);
    }

    public static CustomCacheManager getInstance() {
        return INSTANCE;
    }
}

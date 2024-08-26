package com.kir138.customCash;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomCacheManager2 {
    private final Map<String, City2> cache;

    public CustomCacheManager2() {
        this.cache = new HashMap<>();
    }

    public void putCity(String id, City2 city) {
        cache.put(id, city);
    }
}

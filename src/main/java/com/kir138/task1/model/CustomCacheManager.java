package com.kir138.task1.model;

import com.kir138.task1.model.dto.LocationResponse;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomCacheManager {
    private static final CustomCacheManager INSTANCE = new CustomCacheManager();
    private final Map<Long, LocationResponse> cache;

    public CustomCacheManager() {
        this.cache = new HashMap<>();
    }

    public void updateCity(Long id, LocationResponse locationResponse) {
        cache.put(id, locationResponse);
    }
}

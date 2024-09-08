package com.kir138.task1.model;

import com.kir138.task1.model.dto.CityDto;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomCacheManager {
    private static final CustomCacheManager INSTANCE = new CustomCacheManager();
    private final Map<Long, CityDto> cache;

    public CustomCacheManager() {
        this.cache = new HashMap<>();
    }

    public void updateCity(Long id, CityDto cityDto) {
        cache.put(id, cityDto);
    }
}

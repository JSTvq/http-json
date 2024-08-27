package com.kir138.task1.cache;

import com.kir138.task1.model.dto.CityDto;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomCacheManager {
    private final Map<Long, CityDto> cache = new HashMap<>();

    public void updateCity(Long id, CityDto cityDto) {
        cache.put(id, cityDto);
    }


}

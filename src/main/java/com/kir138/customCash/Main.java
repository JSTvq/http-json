package com.kir138.customCash;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ApiService2 apiService2 = new ApiService2();
        CustomCacheManager2 cacheManager = new CustomCacheManager2();

        try {
            List<City2> cities = apiService2.getTopCities(150);
            for (City2 city : cities) {
                cacheManager.putCity(city.getId(), city);
            }

            Map<String, City2> cache = cacheManager.getCache();
            for (String cityId : cache.keySet()) {
                City2 cachedCity = cache.get(cityId);
                System.out.println(cachedCity.getCityName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

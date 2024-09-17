package com.kir138.task1.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.model.dto.LocationResponse;
import com.kir138.task1.model.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@RequiredArgsConstructor
public class AccuWeatherClient {
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final AccuWeatherUrlBuilder accuWeatherUrlBuilder;

    public String getLocationKey(String city) throws IOException {
        HttpUrl httpUrl = accuWeatherUrlBuilder.buildLocationKeyUrl(city);
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        String jsonData = null;
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new NullPointerException("Не найдена локация");
            }

            if (response.body() != null) {
                jsonData = response.body().string();
            }
        }
        LocationResponse[] locations = objectMapper.readValue(jsonData, LocationResponse[].class);
        if (locations == null || locations.length == 0) {
            throw new IOException("Не найдена локация");
        }

        if (locations[0].getKey() == null) {
            throw new IOException("Не найдена локация");
        }
        return locations[0].getKey();
    }

    public WeatherResponse getWeatherForecast(String locationKey) throws IOException {
        HttpUrl httpUrl = accuWeatherUrlBuilder.buildWeatherForecastUrl(locationKey);
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Не удалось получить прогноз погоды для ключа локации: " + locationKey);
            }
            String jsonData = null;
            if (response.body() != null) {
                jsonData = response.body().string();
            }
            return objectMapper.readValue(jsonData, WeatherResponse.class);
        }
    }

    public LocationResponse[] getTopCities(int num) throws IOException {
        HttpUrl httpUrl = accuWeatherUrlBuilder.buildTopCities(num);
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Не удалось получить список топ городов");
            }

            String topCities = null;
            if (response.body() != null) {
                topCities = response.body().string();
            }
            return objectMapper.readValue(topCities, LocationResponse[].class);
        }
    }
}


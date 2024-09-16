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

    private final static String ACCUWEATHER_HOST = "dataservice.accuweather.com";
    private final String apiKey = "oMGV7LUtNeYunfdr0pk6juw9bJNv3LwV"; //TODO ключ как-то прокинуть по другому
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public String getLocationKey(String city) throws IOException {

        HttpUrl httpUrl = new HttpUrl.Builder() //TODO Вот такие http запросы вынести отдельно
                .scheme("http")
                .host(ACCUWEATHER_HOST)
                .addPathSegment("locations")
                .addPathSegment("v1")
                .addPathSegment("cities")
                .addPathSegment("search")
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("q", city)
                .build();

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

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(ACCUWEATHER_HOST)
                .addPathSegment("forecasts")
                .addPathSegment("v1")
                .addPathSegment("daily")
                .addPathSegment("5day")
                .addPathSegment(locationKey)
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("language", "ru-ru")
                .addQueryParameter("metric", "true")
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Не удалось получить прогноз погоды для ключа локации: " + locationKey); //нужно это или др исключение?!
            }
            String jsonData = null;
            if (response.body() != null) {
                jsonData = response.body().string();
            }
            return objectMapper.readValue(jsonData, WeatherResponse.class);
        }
    }

    public LocationResponse[] getTopCities(int num) throws IOException {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(ACCUWEATHER_HOST)
                .addPathSegment("locations")
                .addPathSegment("v1")
                .addPathSegment("topcities")
                .addPathSegment(String.valueOf(num))
                .addQueryParameter("apikey", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Не удалось получить список топ городов"); //нужно это или др исключение?!
            }

            String topCities = null;
            if (response.body() != null) {
                topCities = response.body().string();
            }
            return objectMapper.readValue(topCities, LocationResponse[].class);
        }
    }
}


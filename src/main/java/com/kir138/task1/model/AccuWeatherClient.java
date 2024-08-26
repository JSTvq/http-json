package com.kir138.task1.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.weatherInfo.Location;
import com.kir138.task1.weatherInfo.WeatherResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class AccuWeatherClient {

    private final String apiKey = "oMGV7LUtNeYunfdr0pk6juw9bJNv3LwV";
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public AccuWeatherClient() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        objectMapper = new ObjectMapper();
    }

    public String getLocationKey(String city) throws IOException {

        String url = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + apiKey + "&q=" + city;

        Request request = new Request.Builder()
                .url(url)
                .build();

        /**
         * Второй вариант использования кеша?
         * Request request = new Request.Builder()
         *                 .url(url)
         *                 .cacheControl(new CacheControl.Builder()
         *                         .maxAge(5, TimeUnit.MINUTES)
         *                         .build())
         *                 .build();
         */

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String jsonData = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();

            Location[] locations = objectMapper.readValue(jsonData, Location[].class);

            if (locations.length > 0) {
                return locations[0].getKey();
            }
        }
        return null;
    }

    public WeatherResponse getWeatherForecast(String locationKey) throws IOException {

        String url = "http://dataservice.accuweather.com/forecasts/v1/daily/5day/" + locationKey + "?apikey="
                + apiKey + "&language=ru-ru&metric=true";

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String jsonData = response.body().string();
            return objectMapper.readValue(jsonData, WeatherResponse.class);
        }
        return null;
    }

    public String inputCity() {
        System.out.println("В каком городе из списка смотрим погоду?");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public List<City> getTopCities(int num) throws IOException {

        List<City> cityList = new ArrayList<>();
        String url = "http://dataservice.accuweather.com/locations/v1/topcities/" + num + "?apikey=" + apiKey;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String topCities = response.body().string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(topCities);
            for (JsonNode add : jsonNode) {
                Long id = add.get("Key").asLong();
                String country = add.get("LocalizedName").asText();
                String cities = add.get("Country").get("LocalizedName").asText();
                City city = City.builder()
                        .id(id)
                        .cityName(cities)
                        .country(country)
                        .build();
                cityList.add(city);
            }
        }
        return cityList;
    }
}

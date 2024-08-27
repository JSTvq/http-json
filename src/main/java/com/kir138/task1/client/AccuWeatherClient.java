package com.kir138.task1.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.dto.Location;
import com.kir138.task1.model.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AccuWeatherClient {

    private final static String ACCUWEATHER_HOST = "dataservice.accuweather.com";
    private final String apiKey = "oMGV7LUtNeYunfdr0pk6juw9bJNv3LwV";
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public String getLocationKey(String city) throws IOException {
        HttpUrl build = new HttpUrl.Builder()
                .host(ACCUWEATHER_HOST)
                .addPathSegment("locations")
                .addPathSegment("v1")
                .addPathSegment("cities")
                .addPathSegment("search")
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("q", city)
                .build();

        Request request = new Request.Builder()
                .url(build)
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

        try (Response response = okHttpClient.newCall(request).execute();) {
            if (response.isSuccessful()) {
                assert response.body() != null;

                String jsonData = response.body().string();

                Location[] locations = objectMapper.readValue(jsonData, Location[].class);

                if (locations.length > 0) {
                    return locations[0].getKey();
                }
            }
        }
        //плохо
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
        throw new RuntimeException("Failed to get weather forecast");
    }

    public List<CityDto> getTopCities(int num) throws IOException {
        String url = "http://dataservice.accuweather.com/locations/v1/topcities/" + num + "?apikey=" + apiKey;
        Request request = new Request.Builder()
                .url(url)
                .build();
        System.out.println("Request: " + request);
        Response response = okHttpClient.newCall(request).execute();
        System.out.println("Response: " + response);

        if (!response.isSuccessful()) {
            throw new RuntimeException("Failed to get top cities");
        }

        String topCities = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(topCities);

        List<CityDto> cityDtoList = new ArrayList<>();
        for (JsonNode add : jsonNode) {
            Long id = add.get("Key").asLong();
            String country = add.get("LocalizedName").asText();
            String cities = add.get("Country").get("LocalizedName").asText();
            CityDto cityDto = CityDto.builder()
                    .id(id)
                    .cityName(cities)
                    .country(country)
                    .build();
            cityDtoList.add(cityDto);
        }


        return cityDtoList;
    }
}

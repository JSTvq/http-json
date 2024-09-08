package com.kir138.task1.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.dto.Location2;
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

        HttpUrl httpUrl = new HttpUrl.Builder()
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

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new NullPointerException("Не найдена локация");
        }
        assert response.body() != null;
        String jsonData = response.body().string();
        Location2[] locations = objectMapper.readValue(jsonData, Location2[].class);
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

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            System.out.println("что-то неудачное");
        }
        assert response.body() != null;
        String jsonData = response.body().string();
        return objectMapper.readValue(jsonData, WeatherResponse.class);
    }

    public List<CityDto> getTopCities(int num) throws IOException {
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

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            System.out.println("что-то пошло не так");
        }
        assert response.body() != null;
        String topCities = response.body().string();
        Location2[] locations = objectMapper.readValue(topCities, Location2[].class);

        List<CityDto> cityDtoList = new ArrayList<>();
        for (Location2 location : locations) {
            System.out.println(location.getLocalizedName());
            cityDtoList.add(CityDto.builder()
                    .cityName(location.getLocalizedName())
                    .build());
        }
        return cityDtoList;
    }
}


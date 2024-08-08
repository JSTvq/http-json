package com.kir138.task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WeatherService {

    private final OkHttpClient okHttpClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public WeatherService(String apiKey) {
        this.apiKey = apiKey;
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
    }

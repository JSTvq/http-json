package com.kir138.task1.model;

import okhttp3.HttpUrl;

public class AccuWeatherUrlBuilder {
    private final static String ACCUWEATHER_HOST = "dataservice.accuweather.com";
    private final String apiKey = "oMGV7LUtNeYunfdr0pk6juw9bJNv3LwV"; //TODO ключ как-то прокинуть по другому

    public HttpUrl buildLocationKeyUrl(String city) {
        return new HttpUrl.Builder()
                .scheme("http")
                .host(ACCUWEATHER_HOST)
                .addPathSegment("locations")
                .addPathSegment("v1")
                .addPathSegment("cities")
                .addPathSegment("search")
                .addQueryParameter("apikey", apiKey)
                .addQueryParameter("q", city)
                .build();
    }

    public HttpUrl buildWeatherForecastUrl(String locationKey) {
        return new HttpUrl.Builder()
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
    }

    public HttpUrl buildTopCities(int num) {
        return new HttpUrl.Builder()
                .scheme("http")
                .host(ACCUWEATHER_HOST)
                .addPathSegment("locations")
                .addPathSegment("v1")
                .addPathSegment("topcities")
                .addPathSegment(String.valueOf(num))
                .addQueryParameter("apikey", apiKey)
                .build();
    }
}

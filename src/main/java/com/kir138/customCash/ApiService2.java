package com.kir138.customCash;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiService2 {
    private static final String API_KEY = "oMGV7LUtNeYunfdr0pk6juw9bJNv3LwV";
    private final OkHttpClient okHttpClient = new OkHttpClient();

    public List<City2> getTopCities(int num) throws IOException {
        List<City2> cityList = new ArrayList<>();
        String url = "http://dataservice.accuweather.com/locations/v1/topcities/" + num + "?apikey=" + API_KEY;
        Request request = new Request.Builder()
            .url(url)
            .build();
        Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String text = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(text);
                for (JsonNode add : jsonNode) {
                    City2 city = new City2();
                    city.setId(add.get("Key").asText());
                    city.setCountry(add.get("LocalizedName").asText());
                    city.setCityName(add.get("Country").get("LocalizedName").asText());
                    cityList.add(city);
                }
        }
        return cityList;
    }
}

package com.kir138.customCash;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApiService {
    private static final String API_KEY = "oMGV7LUtNeYunfdr0pk6juw9bJNv3LwV";
    private static final String BASE_URL = "http://dataservice.accuweather.com/locations/v1/topcities/";

    public List<City2> getTopCities(int group) throws IOException {
        List<City2> cities = new ArrayList<>();
        URL url = new URL(BASE_URL + group + "?apikey=" + API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("HTTP GET Request Failed with Error code : " + conn.getResponseCode());
        }

        Scanner scanner = new Scanner(conn.getInputStream());
        String inline = "";
        while (scanner.hasNext()) {
            inline += scanner.nextLine();
        }
        scanner.close();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(inline);
        for (JsonNode cityNode : node) {
            City2 city = new City2();
            city.setId(cityNode.get("Key").asText());
            city.setCityName(cityNode.get("LocalizedName").asText());
            city.setCountry(cityNode.get("Country").get("LocalizedName").asText());
            cities.add(city);
        }

        return cities;
    }
}
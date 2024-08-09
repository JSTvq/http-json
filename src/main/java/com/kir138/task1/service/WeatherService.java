package com.kir138.task1.service;

import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.weatherInfo.WeatherResponse;
import lombok.Getter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherService {
    private final AccuWeatherClient accuWeatherClient;
    @Getter
    private String city;

    public WeatherService(AccuWeatherClient accuWeatherClient) {
        this.accuWeatherClient = accuWeatherClient;
    }

    public void run(String[] args) throws IOException {
        if (args.length > 0) {
            city = args[0];
        } else {
            city = accuWeatherClient.inputCity();
        }

        String locationKey = accuWeatherClient.getLocationKey(city);
        if (locationKey != null) {
            WeatherResponse weatherResponse = accuWeatherClient.getWeatherForecast(locationKey);
            if (weatherResponse != null) {
                displayWeatherForecast(weatherResponse);
            }
        } else {
            System.out.println("город не найден");
        }
        System.exit(0);
    }

    public void displayWeatherForecast(WeatherResponse weatherResponse) {
        for (WeatherResponse.WeatherList item : weatherResponse.getList()) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.getDateText());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String date = zonedDateTime.format(dateTimeFormatter);
            String icon = item.getDay().getIconPhrase();
            double temp = item.getTemperature().getValue().getTemp();
            System.out.println("В городе " + city + " на дату " + date + " ожидается: " + icon + ", " +
                    "температура " + temp + "°C");
        }
    }
}

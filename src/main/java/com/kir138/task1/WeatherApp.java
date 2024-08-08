package com.kir138.task1;

import lombok.Getter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    private final WeatherService weatherService;
    @Getter
    private String city;

    public WeatherApp(String apiKey) {
        this.weatherService = new WeatherService(apiKey);
    }

    public void run(String[] args) throws IOException {
        if (args.length > 0) {
            city = args[0];
        } else {
            city = inputCity();
        }

        String locationKey = weatherService.getLocationKey(city);
        if (locationKey != null) {
            WeatherResponse weatherResponse = weatherService.getWeatherForecast(locationKey);
            if (weatherResponse != null) {
                displayWeatherForecast(weatherResponse);
            }
        } else {
            System.out.println("город не найден");
        }
        System.exit(0);
    }

    public static String inputCity() {
        System.out.println("В каком городе нужно узнать погоду?");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public void displayWeatherForecast(WeatherResponse weatherResponse) {
        for (WeatherResponse.WeatherList item : weatherResponse.getList()) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.getDateText());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String date = zonedDateTime.format(dateTimeFormatter);
            String icon = item.getDay().getIconPhrase();
            double temp = item.getTemperature().getValue().getTemp();
            System.out.println("В городе " + city + " на дату " + date + " ожидается: " + icon + ", температура " + temp + "°C");
        }
    }
}

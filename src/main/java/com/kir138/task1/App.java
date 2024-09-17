package com.kir138.task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.AccuWeatherUrlBuilder;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.service.WeatherService;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        /**
         * Реализовать корректный вывод информации о текущей погоде. Создать класс WeatherResponse
         * и десериализовать ответ сервера. Выводить пользователю только текстовое описание
         * погоды и температуру в градусах цельсия.
         * Реализовать корректный выход из программы
         * Реализовать вывод погоды на следующие 5 дней в формате
         * В городе CITY на дату DATE ожидается WEATHER_TEXT, температура - TEMPERATURE
         * где CITY, DATE, WEATHER_TEXT и TEMPERATURE - уникальные значения для каждого дня.
         */

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        AccuWeatherUrlBuilder accuWeatherUrlBuilder = new AccuWeatherUrlBuilder();

        AccuWeatherClient accuWeatherClient = new AccuWeatherClient(okHttpClient, objectMapper, accuWeatherUrlBuilder);
        CustomCacheManager customCacheManager = new CustomCacheManager();
        WeatherCityRepository weatherCityRepository = new WeatherCityRepository();
        WeatherHistoryMapper weatherHistoryMapper = new WeatherHistoryMapper();
        Scanner scanner = new Scanner(System.in);
        WeatherService weatherService = new WeatherService(accuWeatherClient, weatherCityRepository, customCacheManager,
                weatherHistoryMapper, scanner);

        weatherService.createTable("weather");
        weatherService.run();
        weatherService.deleteCityById(5L);
        List<CityDto> main2 = weatherService.findAllCities();
        for (CityDto cityDto : main2) {
            System.out.println(cityDto);
        }
        System.out.println(weatherService.findCityById(7L));
        System.out.println(weatherService.findCityByName("Miami"));
    }
}

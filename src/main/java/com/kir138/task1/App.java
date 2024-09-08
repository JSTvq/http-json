package com.kir138.task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.service.WeatherService;
import okhttp3.OkHttpClient;
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

        AccuWeatherClient accuWeatherClient = new AccuWeatherClient(okHttpClient, objectMapper);
        CustomCacheManager customCacheManager = new CustomCacheManager();
        WeatherCityRepository weatherCityRepository = new WeatherCityRepository();
        WeatherService weatherService = new WeatherService(accuWeatherClient, weatherCityRepository, customCacheManager);

        weatherService.run();
    }
}

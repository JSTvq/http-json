package com.kir138.task1;

import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.service.WeatherService;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        /**
         * Реализовать корректный вывод информации о текущей погоде. Создать класс WeatherResponse
         * и десериализовать ответ сервера. Выводить пользователю только текстовое описание
         * погоды и температуру в градусах цельсия.
         * Реализовать корректный выход из программы
         * Реализовать вывод погоды на следующие 5 дней в формате
         * В городе CITY на дату DATE ожидается WEATHER_TEXT, температура - TEMPERATURE
         * где CITY, DATE, WEATHER_TEXT и TEMPERATURE - уникальные значения для каждого дня.
         */

        AccuWeatherClient accuWeatherClient = new AccuWeatherClient();
        WeatherService weatherService = new WeatherService(accuWeatherClient);

        weatherService.run();
        /*for (int i = 0; i < 5; i++) {
            weatherService.run();
        }*/
    }
}
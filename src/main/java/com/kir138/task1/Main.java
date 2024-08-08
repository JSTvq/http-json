package com.kir138.task1;

import java.io.IOException;

public class Main {
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

        String apiKey = "oMGV7LUtNeYunfdr0pk6juw9bJNv3LwV";
        WeatherApp weatherApp = new WeatherApp(apiKey);
        weatherApp.run(args);
    }
}
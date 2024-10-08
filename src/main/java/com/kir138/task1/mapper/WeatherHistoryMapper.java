package com.kir138.task1.mapper;

import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.dto.WeatherResponse;
import com.kir138.task1.model.entity.WeatherHistory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeatherHistoryMapper {
    public CityDto toCityDto(WeatherHistory weatherHistory) {
        return CityDto.builder()
                .id(weatherHistory.getId())
                .cityName(weatherHistory.getCityName())
                .date(weatherHistory.getRqDateTime())
                .weatherConditions(weatherHistory.getWeatherConditions())
                .temperature(weatherHistory.getTemperature())
                .date(weatherHistory.getRqDateTime())
                .build();
    }

    public List<WeatherHistory> weatherForecast(WeatherResponse weatherResponse, String city_name) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return weatherResponse.getList().stream()
                .map(item -> {
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(item.getDateText(), inputFormatter);
                    LocalDate localDate = offsetDateTime.toLocalDate();
                    String weather_conditions = item.getDay().getIconPhrase();
                    double temperature = item.getTemperature().getValue().getTemp();
                    System.out.println("В городе " + city_name + " на дату " + localDate + " ожидается: " + weather_conditions + ", " +
                        "температура " + temperature + "°C");

                    return WeatherHistory.builder()
                        .cityName(city_name)
                        .rqDateTime(localDate)
                        .temperature(temperature)
                        .weatherConditions(weather_conditions)
                        .build();
                })
                .toList();
    }
}

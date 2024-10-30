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
                .date(weatherHistory.getRqLocalDate())
                .weatherConditions(weatherHistory.getWeatherConditions())
                .temperature(weatherHistory.getTemperature())
                .build();
    }

    public WeatherHistory toWeatherHistory(CityDto cityDto) {
        return WeatherHistory.builder()
                .id(cityDto.getId())
                .cityName(cityDto.getCityName())
                .rqLocalDate(cityDto.getDate())
                .weatherConditions(cityDto.getWeatherConditions())
                .temperature(cityDto.getTemperature())
                .build();
    }

    public List<WeatherHistory> weatherForecast(WeatherResponse weatherResponse, String cityName) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return weatherResponse.getList().stream()
                .map(item -> {
                    OffsetDateTime offsetDateTime = OffsetDateTime.parse(item.getDateText(), inputFormatter);
                    LocalDate localDate = offsetDateTime.toLocalDate();
                    String weather_conditions = item.getDay().getIconPhrase();
                    double temperature = item.getTemperature().getValue().getTemp();
                    System.out.println("В городе " + cityName + " на дату " + localDate + " ожидается: " + weather_conditions + ", " +
                        "температура " + temperature + "°C");

                    return WeatherHistory.builder()
                        .cityName(cityName)
                        .rqLocalDate(localDate)
                        .temperature(temperature)
                        .weatherConditions(weather_conditions)
                        .build();
                })
                .toList();
    }
}

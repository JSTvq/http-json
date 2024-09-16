package com.kir138.task1.mapper;

import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.dto.WeatherResponse;
import com.kir138.task1.model.entity.WeatherHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        List<WeatherHistory> cityDtoList = new ArrayList<>(); //TODO маппер переписать через стрим
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (WeatherResponse.WeatherList item : weatherResponse.getList()) {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(item.getDateText(), inputFormatter);
            LocalDate localDate = offsetDateTime.toLocalDate();
            LocalDateTime localDateTime = localDate.atStartOfDay();

            String weather_conditions = item.getDay().getIconPhrase();
            double temperature = item.getTemperature().getValue().getTemp();
            System.out.println("В городе " + city_name + " на дату " + localDateTime + " ожидается: " + weather_conditions + ", " +
                    "температура " + temperature + "°C");

            // Добавляем запись в список
            cityDtoList.add(WeatherHistory.builder()
                    .cityName(city_name)
                    .rqDateTime(localDateTime)
                    .temperature(temperature)
                    .weatherConditions(weather_conditions)
                    .build());
        }
        return cityDtoList;
    }
}

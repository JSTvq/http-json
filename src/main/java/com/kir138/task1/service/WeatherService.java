package com.kir138.task1.service;

import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.model.dto.WeatherResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Getter
@AllArgsConstructor
public class WeatherService {
    private final AccuWeatherClient accuWeatherClient;
    private final WeatherCityRepository weatherCityRepository;
    private final CustomCacheManager customCacheManager;

    public void run() {

        Map<Long, CityDto> listCities = listCitiesCache();

        while (true) {
            String citySelectedUser = getUserCityInput();

            if (citySelectedUser.equalsIgnoreCase("exit")) {
                System.out.println("Программа завершена.");
                break;
            }

            CityDto selectedCity = listCities.values().stream()
                    .filter(city -> city.getCityName().equalsIgnoreCase(citySelectedUser))
                    .findFirst()
                    .orElse(null);

            if (selectedCity == null) {
                System.out.println("попробуйте ввести город еще раз");
                continue;
            }

            try {
                String locationKey = accuWeatherClient.getLocationKey(citySelectedUser);
                WeatherResponse weatherResponse = accuWeatherClient.getWeatherForecast(locationKey);
                if (weatherResponse != null) {
                    List<CityDto> cityDtoList = weatherForecast(weatherResponse, citySelectedUser);
                    for (CityDto cityDto : cityDtoList) {
                        weatherCityRepository.save(cityDto);
                    }
                } else {
                    System.out.println("Не удалось получить прогноз погоды. " +
                            "Проверьте лимиты запросов или повторите позже.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getUserCityInput() {
        return inputCity();
    }

    public String inputCity() {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.println("В каком городе из списка смотрим погоду? (Введите 'exit' для завершения)");
            input = scanner.next();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Программа завершена.");
                System.exit(0);
            }
        } while (input.isEmpty());

        return input;
    }

    public List<CityDto> weatherForecast(WeatherResponse weatherResponse, String city) {
        List<CityDto> cityDtoList = new ArrayList<>();
        for (WeatherResponse.WeatherList item : weatherResponse.getList()) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.getDateText());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String date = zonedDateTime.format(dateTimeFormatter);
            String icon = item.getDay().getIconPhrase();
            double temp = item.getTemperature().getValue().getTemp();
            System.out.println("В городе " + city + " на дату " + date + " ожидается: " + icon + ", " +
                    "температура " + temp + "°C");

            cityDtoList.add(CityDto.builder()
                    .cityName(city)
                    .date(date)
                    .weatherConditions(icon)
                    .temperature(temp)
                    .build());
        }
        return cityDtoList;
    }

    public Map<Long, CityDto> listCitiesCache() {
        try {
            List<CityDto> cities = accuWeatherClient.getTopCities(150);
            long id = 1;
            for (CityDto cityDto : cities) {
                customCacheManager.updateCity(id, cityDto);
                id++;
            }
            return customCacheManager.getCache();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

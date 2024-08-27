package com.kir138.task1.service;

import com.kir138.task1.client.AccuWeatherClient;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.cache.CustomCacheManager;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.model.dto.WeatherResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Getter
@RequiredArgsConstructor
public class WeatherService {
    private final AccuWeatherClient accuWeatherClient;
    private final WeatherCityRepository weatherCityRepository;
    private final CustomCacheManager customCacheManager;

    public void run() throws IOException {
        Map<Long, CityDto> longCityMap = listCitiesCache();
//        displayCityList(listCities);
        String citySelectedUser = getUserCityInput();

        if (!longCityMap.containsValue(citySelectedUser)) {
            System.out.println("Введенный город не найден. \nОбновите список городов и попробуйте снова");
            return;
        }

        String city = citySelectedUser;


        String locationKey = accuWeatherClient.getLocationKey(city);
        if (locationKey == null) {
            System.out.println("Введенный город не найден, обновите список городов");
            return;
        }

        WeatherResponse weatherResponse = accuWeatherClient.getWeatherForecast(locationKey);
        if (weatherResponse != null) {
            List<CityDto> cityDtoList = weatherForecast(weatherResponse, city);
            for (CityDto cityDto1 : cityDtoList) {
                weatherCityRepository.save(cityDto1);
            }
        }
    }

    private void displayCityList(List<String> listCities) {
        for (String city : listCities) {
            System.out.println(city);
        }
    }

    private String getUserCityInput() {
        System.out.println("В каком городе из списка смотрим погоду?");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
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
            for (CityDto cityDto : cities) {
                customCacheManager.updateCity(cityDto.getId(), cityDto);
            }

            return customCacheManager.getCache();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

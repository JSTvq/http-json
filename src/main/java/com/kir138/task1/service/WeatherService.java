package com.kir138.task1.service;

import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.dto.LocationResponse;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.model.dto.WeatherResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class WeatherService {
    private final AccuWeatherClient accuWeatherClient;
    private final WeatherCityRepository weatherCityRepository;
    private final CustomCacheManager customCacheManager;
    private final WeatherHistoryMapper weatherHistoryMapper;

    public void run() {

        Map<Long, LocationResponse> listCities = listCitiesCache();

        for (LocationResponse cityName : listCities.values()) {
            System.out.println(cityName.getLocalizedName());
        }

        while (true) {
            String citySelectedUser = inputCity();
            if (citySelectedUser.equalsIgnoreCase("exit")) {
                System.out.println("Программа завершена.");
                break;
            }

            Optional<LocationResponse> selectedCity = listCities.values().stream()
                    .filter(city -> city.getLocalizedName().trim().equalsIgnoreCase(citySelectedUser.trim()))
                    .findFirst();

            if (selectedCity.isEmpty()) {
                System.out.println("попробуйте ввести город еще раз");
                continue;
            }

            try {
                String locationKey = accuWeatherClient.getLocationKey(citySelectedUser);
                WeatherResponse weatherResponse = accuWeatherClient.getWeatherForecast(locationKey);
                    List<WeatherHistory> cityDtoList = weatherHistoryMapper.weatherForecast(weatherResponse, citySelectedUser);
                    for (WeatherHistory weatherHistory : cityDtoList) {
                        weatherCityRepository.save(weatherHistory);
                    }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String inputCity() {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            System.out.println("В каком городе из списка смотрим погоду? (Введите 'exit' для завершения)");
            input = scanner.next();
        } while (input.isEmpty());

        return input;
    }

    public Map<Long, LocationResponse> listCitiesCache() {
        try {
            LocationResponse[] locationResponses = accuWeatherClient.getTopCities(150);
            long id = 1;
            for (LocationResponse responses : locationResponses) {
                customCacheManager.updateCity(id, responses);
                id++;
            }
            return customCacheManager.getCache();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CityDto findCityById(Long id) {
        return weatherCityRepository.findById(id)
                .map(weatherHistoryMapper::toCityDto)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<CityDto> findCityByName(String cityName) {
        return weatherCityRepository.findByNameCity(cityName)
                .stream()
                .map(weatherHistoryMapper::toCityDto)
                .toList();
    }

    public List<CityDto> findAllCities() {
        return weatherCityRepository.findAll()
                .stream()
                .map(weatherHistoryMapper::toCityDto)
                .toList();
    }

    public void deleteCityById(Long id) {
        weatherCityRepository.deleteCityById(id);
    }

    public void createTable(String table) {
        weatherCityRepository.createTable(table);
    }
}

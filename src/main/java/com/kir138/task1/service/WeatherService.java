package com.kir138.task1.service;

import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.dto.LocationResponse;
import com.kir138.task1.model.dto.WeatherResponse;
import com.kir138.task1.model.entity.City;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.repository.CrudRepository;
import com.kir138.task1.repository.WeatherCityHibernateRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Getter
@RequiredArgsConstructor
public class WeatherService {
    private final AccuWeatherClient accuWeatherClient;
    private final CrudRepository<WeatherHistory, Long> weatherCityRepository;
    private final CustomCacheManager customCacheManager;
    private final WeatherHistoryMapper weatherHistoryMapper;
    private final Scanner scanner;
    private final WeatherCityHibernateRepository weatherCityHibernateRepository;

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
                    weatherCityHibernateRepository.save(weatherHistory);
                    //boolean add1 = weatherCityHibernateRepository.cityExistsInMainCity(weatherHistory.getCityName());
                    boolean add = weatherCityHibernateRepository.cityExistsInCity(weatherHistory.getCityName());
                    if (!add) {
                        weatherCityHibernateRepository.saveNameCity(City.builder()
                                        .cityName(weatherHistory.getCityName())
                                        .build());
                    } else {
                        System.out.println("Название города уже было сохранено");
                        break;
                    }

                    /**
                     * TODO
                     * Добавить сюда методы из класса WeatherCityHibernateRepository для проверки есть ли такой
                     * город в таблице City. Если нет, то добавить в единственном числе.
                     */
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String inputCity() {
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

    /*public void createTable(String table) {
        weatherCityRepository.createTable(table);
    }*/
}

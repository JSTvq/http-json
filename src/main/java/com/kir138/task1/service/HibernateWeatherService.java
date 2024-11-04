package com.kir138.task1.service;

import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.repository.WeatherCityHibernateRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class HibernateWeatherService {
    private final WeatherCityHibernateRepository weatherCityHibernateRepository;
    private final WeatherHistoryMapper weatherHistoryMapper;

    public CityDto findCityById(Long id) {
        return weatherCityHibernateRepository.findById(id)
                .map(weatherHistoryMapper::toCityDto)
                .orElseThrow(() -> new IllegalArgumentException("город с таким Id не найден"));
    }

    public List<CityDto> findCityByName(String cityName) {
        return weatherCityHibernateRepository.findByNameCity(cityName)
                .stream()
                .map(weatherHistoryMapper::toCityDto)
                .toList();
    }

    public List<CityDto> findAllCities() {
        return weatherCityHibernateRepository.findAll()
                .stream()
                .map(weatherHistoryMapper::toCityDto)
                .toList();
    }

    public CityDto save(WeatherHistory weatherHistory) {
        return weatherHistoryMapper.toCityDto(weatherCityHibernateRepository.save(weatherHistory));
    }

    public void deleteCityById(Long id) {
        weatherCityHibernateRepository.deleteCityById(id);
    }
}

package com.kir138.task1.repository;

import com.kir138.task1.model.entity.WeatherHistory;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, K> {

    List<T> findAll();

    Optional<T> findById(K id);

    T save(T t);

    void deleteCityById(K id);

    List<WeatherHistory> findByNameCity(String nameCity);
}

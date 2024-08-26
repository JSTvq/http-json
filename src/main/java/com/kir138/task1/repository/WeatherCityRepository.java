package com.kir138.task1.repository;

import com.kir138.task1.constants.SqlQuery;
import com.kir138.task1.model.City;
import com.kir138.task1.sqlConnect.PgConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class WeatherCityRepository implements CrudRepository<City, Long>{

    private final Connection connection = PgConnect.getConnection();

    @Override
    public List<City> findAll() {
        return null;
    }

    @Override
    public Optional<City> findById(Long id) {
        return Optional.empty();
    }

    public boolean uniqueCity(String city, String date) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.UNIQUE_CITY.getQuery())) {
            preparedStatement.setString(1, city);
            preparedStatement.setString(2, date);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public City save(List<City> listCity) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.INSERT_CITY.getQuery())) {

            for (City city : listCity) {
                if (!uniqueCity(city.getCityName(), city.getDate())) {
                    preparedStatement.setString(1, city.getCityName());
                    preparedStatement.setString(2, city.getDate());
                    preparedStatement.setString(3, city.getWeatherConditions());
                    preparedStatement.setDouble(4, city.getTemperature());
                    preparedStatement.executeUpdate();
                }
                return city;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        System.out.println();
    }
}

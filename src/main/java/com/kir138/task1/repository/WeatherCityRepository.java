package com.kir138.task1.repository;

import com.kir138.task1.constants.SqlQuery;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.sqlConnect.PgConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class WeatherCityRepository implements CrudRepository<CityDto, Long>{

    private final Connection connection = PgConnect.getConnection();

    @Override
    public List<CityDto> findAll() {
        return null;
    }

    @Override
    public Optional<CityDto> findById(Long id) {
        return Optional.empty();
    }

    public boolean existsByCityAndDate(String city, String date) {
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
    public CityDto save(CityDto cityDto) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.INSERT_CITY.getQuery())) {
                if (!existsByCityAndDate(cityDto.getCityName(), cityDto.getDate())) {
                    preparedStatement.setString(1, cityDto.getCityName());
                    preparedStatement.setString(2, cityDto.getDate());
                    preparedStatement.setString(3, cityDto.getWeatherConditions());
                    preparedStatement.setDouble(4, cityDto.getTemperature());
                    preparedStatement.executeUpdate();
                }
                return cityDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        System.out.println();
    }
}

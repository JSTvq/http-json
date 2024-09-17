package com.kir138.task1.repository;

import com.kir138.task1.constants.SqlQuery;
import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.sqlConnect.PgConnect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class WeatherCityRepository implements CrudRepository<WeatherHistory, Long>{

    private final Connection connection = PgConnect.getConnection();
    private final WeatherHistoryMapper weatherHistoryMapper;

    public List<WeatherHistory> findByNameCity(String nameCity) {
        List<WeatherHistory> weatherHistories = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.FIND_CITY_NAME.getQuery())) {
            preparedStatement.setString(1, nameCity);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id  = resultSet.getLong("id");
                String cityName = resultSet.getString("city_name");
                String weatherConditions = resultSet.getString("weather_conditions");
                Double temperature = resultSet.getDouble("temperature");
                Date date = resultSet.getDate("rq_date_time");

                WeatherHistory weatherHistory = WeatherHistory.builder()
                        .id(id)
                        .cityName(cityName)
                        .weatherConditions(weatherConditions)
                        .temperature(temperature)
                        .rqDateTime(date.toLocalDate())
                        .build();
                weatherHistories.add(weatherHistory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return weatherHistories;
    }

    @Override
    public Optional<WeatherHistory> findById(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.FIND_CITY_ID.getQuery())) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long idCity = resultSet.getLong("id");
                String cityName = resultSet.getString("city_name");
                String weatherConditions = resultSet.getString("weather_conditions");
                Double temperature = resultSet.getDouble("temperature");
                Date date = resultSet.getDate("rq_date_time");

                WeatherHistory weatherHistory = WeatherHistory.builder()
                        .id(idCity)
                        .cityName(cityName)
                        .weatherConditions(weatherConditions)
                        .temperature(temperature)
                        .rqDateTime(date.toLocalDate())
                        .build();
                return Optional.of(weatherHistory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<WeatherHistory> findAll() {
        List<WeatherHistory> cities = new ArrayList<>();
        try(Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SqlQuery.FIND_ALL_CITY.getQuery())) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String cityName = resultSet.getString("city_name");
                String weatherConditions = resultSet.getString("weather_conditions");
                Double temperature = resultSet.getDouble("temperature");
                Date date = resultSet.getDate("rq_date_time");

                WeatherHistory weatherHistory = WeatherHistory.builder()
                        .id(id)
                        .cityName(cityName)
                        .weatherConditions(weatherConditions)
                        .temperature(temperature)
                        .rqDateTime(date.toLocalDate())
                        .build();
                cities.add(weatherHistory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cities;
    }

    public boolean existsByCityAndDate(String city, Date date) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.UNIQUE_CITY.getQuery())) {
            preparedStatement.setString(1, city);
            preparedStatement.setDate(2, date);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WeatherHistory save(WeatherHistory weatherHistory) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.INSERT_CITY.getQuery())) {
            // Получаем LocalDateTime
            LocalDate localDate = weatherHistory.getRqDateTime();
            // Преобразуем LocalDate в java.sql.Date
            Date sqlDate = Date.valueOf(localDate);
                if (!existsByCityAndDate(weatherHistory.getCityName(), sqlDate)) {
                    preparedStatement.setString(1, weatherHistory.getCityName());
                    preparedStatement.setDate(2, sqlDate);
                    preparedStatement.setString(3, weatherHistory.getWeatherConditions());
                    preparedStatement.setDouble(4, weatherHistory.getTemperature());
                    preparedStatement.executeUpdate();
                }
                return weatherHistory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCityById(Long id) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.DELETE_CITY.getQuery())) {
            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No city found with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createTable(String table) {
        String createTableSql = String.format(SqlQuery.CREATE_TABLE.getQuery(), table);

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSql);   //нужно ли тут обрабатывать execute через if???
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы: " + e.getMessage());
        }
    }
}

package com.kir138.task1.repository;

import com.kir138.task1.constants.SqlQuery;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.sql.Connect.PgConnect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherCityRepositoryTest {

    private WeatherCityRepository weatherCityRepository;
    private Connection connection;

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password = "12341234";

    public void createTable(String table) {
        String createTableSql = String.format(SqlQuery.CREATE_TABLE.getQuery(), table);

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSql);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("Ошибка при создании таблицы");
        }
    }

    public Connection getConenctionTest() {
        try {
            String testUrl = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(testUrl, username, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = getConenctionTest();
        connection.setAutoCommit(false);
        weatherCityRepository = new WeatherCityRepository(connection);
        String createTableSql = String.format(SqlQuery.CREATE_TABLE.getQuery(), "weather");

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSql);
            connection.commit();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        String dropTableSql = "DROP TABLE IF EXISTS weather, weather_test";

        try (Statement statement = connection.createStatement()) {
            statement.execute(dropTableSql);
            connection.commit();
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    @Test
    public void saveNewCityShouldWork() {
        LocalDate localDate = LocalDate.of(2024, 9, 15);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Moscow")
                .rqLocalDate(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        WeatherHistory result = weatherCityRepository.save(weatherHistory);
        Optional<WeatherHistory> findByIdWeatherHistory = weatherCityRepository.findById(result.getId());

        assertThat(findByIdWeatherHistory)
                .get()
                .isNotNull()
                .extracting(WeatherHistory::getCityName, WeatherHistory::getRqLocalDate, WeatherHistory::getWeatherConditions,
                        WeatherHistory::getTemperature)
                .containsExactly(
                        result.getCityName(),
                        result.getRqLocalDate(),
                        result.getWeatherConditions(),
                        result.getTemperature()
                );

        assertThat(result.getId()).isNotNull();
        assertThat(result.getCityName()).isEqualTo("Moscow");
        assertThat(result.getTemperature()).isEqualTo(15.0);
    }

    @Test
    public void saveNewCityShouldWorkException() {
        LocalDate localDate = LocalDate.of(2024, 9, 15);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName(null)
                .rqLocalDate(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        assertThrows(RuntimeException.class, () -> weatherCityRepository.save(weatherHistory));
    }

    @Test
    public void findById() {
        LocalDate localDate = LocalDate.of(2025, 11, 15);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqLocalDate(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(8.0)
                .build();

        WeatherHistory result = weatherCityRepository.save(weatherHistory);
        Long id = weatherHistory.getId();

        Optional<WeatherHistory> search = weatherCityRepository.findById(id);

        assertThat(search)
                .isPresent()
                .get()
                .isNotNull()
                .extracting(WeatherHistory::getCityName, WeatherHistory::getRqLocalDate, WeatherHistory::getWeatherConditions,
                        WeatherHistory::getTemperature)
                .containsExactly(
                        result.getCityName(),
                        result.getRqLocalDate(),
                        result.getWeatherConditions(),
                        result.getTemperature()
                );
    }

    @Test
    public void findAll() {
        WeatherHistory weatherHistory1 = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqLocalDate(LocalDate.of(2020, 3, 22))
                .weatherConditions("Небольшая облачность")
                .temperature(8.0)
                .build();

        WeatherHistory weatherHistory2 = WeatherHistory.builder()
                .cityName("Moscow")
                .rqLocalDate(LocalDate.of(2015, 5, 24))
                .weatherConditions("Солнечно")
                .temperature(15.0)
                .build();

        WeatherHistory weatherHistorySaveRes1 = weatherCityRepository.save(weatherHistory1);
        WeatherHistory weatherHistorySaveRes2 = weatherCityRepository.save(weatherHistory2);
        List<WeatherHistory> resultSearchList = weatherCityRepository.findAll();

        assertThat(resultSearchList).hasSize(2).containsExactlyInAnyOrder(weatherHistorySaveRes1, weatherHistorySaveRes2);
    }

    @Disabled
    public void findAllShouldWorkException() {
        WeatherHistory weatherHistory1 = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqLocalDate(LocalDate.of(2020, 3, 22))
                .weatherConditions("Небольшая облачность")
                .temperature(8.0)
                .build();

        WeatherHistory weatherHistory2 = WeatherHistory.builder()
                .cityName("Moscow")
                .rqLocalDate(LocalDate.of(2015, 5, 24))
                .weatherConditions("Солнечно")
                .temperature(15.0)
                .build();

        weatherCityRepository.save(weatherHistory1);
        weatherCityRepository.save(weatherHistory2);

        List<WeatherHistory> allCities = weatherCityRepository.findAll();
        assertThat(allCities).hasSize(2);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE weather");
            connection.commit();
        } catch (SQLException ignored) {
        }

        assertThatThrownBy(() -> weatherCityRepository.findAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("найти список не удалось");
    }

    @Test
    public void deleteCityById() {
        LocalDate localDate = LocalDate.of(2025, 11, 15);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqLocalDate(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(8.0)
                .build();

        WeatherHistory weatherHistory1 = weatherCityRepository.save(weatherHistory);
        Long id = weatherHistory1.getId();

        Optional<WeatherHistory> beforeDelete = weatherCityRepository.findById(id);
        assertThat(beforeDelete).isPresent();

        weatherCityRepository.deleteCityById(id);

        Optional<WeatherHistory> afterDelete = weatherCityRepository.findById(id);
        assertThat(afterDelete).isNotPresent();
    }

    @Test
    public void findByName() {
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqLocalDate(LocalDate.of(2022, 4, 20))
                .weatherConditions("Небольшая облачность")
                .temperature(8.0)
                .build();

        String cityName = weatherHistory.getCityName();

        weatherCityRepository.save(weatherHistory);
        List<WeatherHistory> weatherHistoryList = weatherCityRepository.findByNameCity(cityName);

        assertThat(weatherHistoryList).containsExactlyInAnyOrder(weatherHistory);
    }
}
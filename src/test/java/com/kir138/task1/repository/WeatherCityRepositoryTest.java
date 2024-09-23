package com.kir138.task1.repository;

import com.kir138.task1.constants.SqlQuery;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.sql.Connect.PgConnect;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherCityRepositoryTest {

    private WeatherCityRepository weatherCityRepository;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = PgConnect.getConenctionTest();
        //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "12341234");
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
        String dropTableSql = "DROP TABLE IF EXISTS weather";

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
                .rqDateTime(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        WeatherHistory result = weatherCityRepository.save(weatherHistory);
        Optional<WeatherHistory> findByIdWeatherHistory = weatherCityRepository.findById(result.getId());

        assertThat(findByIdWeatherHistory)
                .get()
                .isNotNull()
                .extracting(WeatherHistory::getCityName, WeatherHistory::getRqDateTime, WeatherHistory::getWeatherConditions,
                        WeatherHistory::getTemperature)
                .containsExactly(
                        result.getCityName(),
                        result.getRqDateTime(),
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
                .rqDateTime(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        assertThrows(RuntimeException.class, () -> weatherCityRepository.save(weatherHistory));
    }

    /*@Test
    public void findById() {
        LocalDate localDate = LocalDate.of(2025, 11, 15);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqDateTime(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(8.0)
                .build();

        Long id = weatherHistory.getId();

        Optional<WeatherHistory> search = weatherCityRepository.findById(id);

        assertThat(search).isEqualTo(weatherHistory);
    }*/

    @Test
    public void findAll() {
        WeatherHistory weatherHistory1 = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqDateTime(LocalDate.of(2020, 3, 22))
                .weatherConditions("Небольшая облачность")
                .temperature(8.0)
                .build();

        WeatherHistory weatherHistory2 = WeatherHistory.builder()
                .cityName("Moscow")
                .rqDateTime(LocalDate.of(2015, 5, 24))
                .weatherConditions("Солнечно")
                .temperature(15.0)
                .build();

        WeatherHistory weatherHistorySaveRes1 = weatherCityRepository.save(weatherHistory1);
        WeatherHistory weatherHistorySaveRes2 = weatherCityRepository.save(weatherHistory2);
        List<WeatherHistory> resultSearchList = weatherCityRepository.findAll();

        assertThat(resultSearchList).hasSize(2).containsExactlyInAnyOrder(weatherHistorySaveRes1, weatherHistorySaveRes2);
    }

    /*@Test
    public void findAllShouldWorkException() {
        WeatherHistory weatherHistory1 = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqDateTime(LocalDate.of(2020, 3, 22))
                .weatherConditions("Небольшая облачность")
                .temperature(8.0)
                .build();

        WeatherHistory weatherHistory2 = WeatherHistory.builder()
                .cityName("Moscow")
                .rqDateTime(LocalDate.of(2015, 5, 24))
                .weatherConditions("Солнечно")
                .temperature(15.0)
                .build();

        weatherCityRepository.save(weatherHistory1);
        weatherCityRepository.save(weatherHistory2);

        assertThrows(SQLException.class, () -> weatherCityRepository.findAll());
    }*/

    @Test
    public void deleteCityById() {
        LocalDate localDate = LocalDate.of(2025, 11, 15);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Novosibirsk")
                .rqDateTime(localDate)
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
}
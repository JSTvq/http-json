package com.kir138.task1.repository;

import com.kir138.task1.constants.SqlQuery;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.sql.Connect.PgConnect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherCityRepositoryTest {

    private final WeatherCityRepository weatherCityRepository = new WeatherCityRepository();

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = PgConnect.getConnection();

        String createTableSql = String.format(SqlQuery.CREATE_TABLE.getQuery(), "weather");

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSql);
            connection.commit();
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
        Optional<WeatherHistory> findByIdWeatherHistory = weatherCityRepository.findById(result.getId()); //TODO дописать после сейва. С помощью метода findById удостовериться, что данные были сохранены.

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

    /*@Test
    public void saveExistingCity() throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 9, 14);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Moscow")
                .rqDateTime(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        weatherCityRepository.save(weatherHistory);

        WeatherHistory weatherHistoryDuplicate = WeatherHistory.builder()
                .cityName("Moscow")
                .rqDateTime(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        WeatherHistory resultDuplicate = weatherCityRepository.save(weatherHistoryDuplicate);
        assertNotNull(resultDuplicate.getId());
        assertEquals(weatherHistory.getCityName(), resultDuplicate.getCityName());
        assertEquals(weatherHistory.getRqDateTime(), resultDuplicate.getRqDateTime());
        assertEquals(weatherHistory.getWeatherConditions(), resultDuplicate.getWeatherConditions());
        assertEquals(weatherHistory.getTemperature(), resultDuplicate.getTemperature());
    }

    @Test
    public void existsByCityAndDateExists () throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 9, 14);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Moscow")
                .rqDateTime(localDate)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        //LocalDate localDate = localDateTime.toLocalDate();
        boolean result = weatherCityRepository.existsByCityAndDate("Moscow", Date.valueOf(localDate));
        assertTrue(result);
    }

    @Test
    public void existsByCityAndDateNoteExists () throws SQLException {
        LocalDate localDate = LocalDate.of(2024, 9, 14);
        boolean result = weatherCityRepository.existsByCityAndDate("Moscow", Date.valueOf(localDate));
        assertFalse(result);
    }*/

        /*@Test
        public void saveExistingCity() throws SQLException {
            CityDto cityDto = new CityDto(1L, "Moscow", "Russia", "20.20.2018",
                    "Небольшая облачность", 15.0);

            when(resultSet.next()).thenReturn(true);
            CityDto result = weatherCityRepository.save(cityDto);
            assertNotNull(result);
            assertEquals(cityDto, result);
            verify(preparedStatement, never()).executeUpdate();
        }

        @Test
        public void existsByCityAndDateExists () throws SQLException {
            when(resultSet.next()).thenReturn(true);
            boolean result = weatherCityRepository.existsByCityAndDate("Moscow", "15.11.2022");
            assertTrue(result);
        }

        @Test
        public void existsByCityAndDateNoteExists () throws SQLException {
            when(resultSet.next()).thenReturn(false);
            boolean result = weatherCityRepository.existsByCityAndDate("Moscow", "15.11.2022");
            assertFalse(result);
        }*/
}
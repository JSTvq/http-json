package com.kir138.task1.repository;

import com.kir138.task1.constants.SqlQuery;
import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.sqlConnect.PgConnect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(MockitoExtension.class)
public class WeatherCityRepositoryTest {

    private WeatherCityRepository weatherCityRepository;

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = PgConnect.getConnection();

        String createTableSql = String.format(SqlQuery.CREATE_TABLE.getQuery(), "weather");

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSql);
        }

        WeatherHistoryMapper weatherHistoryMapper = new WeatherHistoryMapper();
        weatherCityRepository = new WeatherCityRepository();
    }

        @Test
        public void saveNewCity() throws SQLException {
            LocalDateTime localDateTime = LocalDateTime.of(2024, 9, 14, 1, 12);
            WeatherHistory weatherHistory = WeatherHistory.builder()
                    .cityName("Moscow")
                    .rqDateTime(localDateTime)
                    .weatherConditions("Небольшая облачность")
                    .temperature(15.0)
                    .build();

            WeatherHistory result = weatherCityRepository.save(weatherHistory);

            //TODO дописать после сейва. С помощью метода FindById удостовериться, что данные были сохранены.

            assertNotNull(result); //TODO эти два assert'a переписать через assertJ, в функциональном стиле
            assertEquals(weatherHistory, result);
        }

    @Test
    public void saveExistingCity() throws SQLException {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 9, 14, 1, 12);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Moscow")
                .rqDateTime(localDateTime)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();


        weatherCityRepository.save(weatherHistory);

        WeatherHistory weatherHistoryDuplicate = WeatherHistory.builder()
                .cityName("Moscow")
                .rqDateTime(localDateTime)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        WeatherHistory resultDuplicate = weatherCityRepository.save(weatherHistoryDuplicate);
        //assertNotNull(resultDuplicate.getId());
        assertEquals(weatherHistory.getCityName(), resultDuplicate.getCityName()); //TODO эти assert'ы переписать через assertJ, в функциональном стиле
        assertEquals(weatherHistory.getRqDateTime(), resultDuplicate.getRqDateTime());
        assertEquals(weatherHistory.getWeatherConditions(), resultDuplicate.getWeatherConditions());
        assertEquals(weatherHistory.getTemperature(), resultDuplicate.getTemperature());
    }

    @Test
    public void existsByCityAndDateExists () throws SQLException {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 9, 14, 1, 12);
        WeatherHistory weatherHistory = WeatherHistory.builder()
                .cityName("Moscow")
                .rqDateTime(localDateTime)
                .weatherConditions("Небольшая облачность")
                .temperature(15.0)
                .build();

        //LocalDate localDate = localDateTime.toLocalDate();
        boolean result = weatherCityRepository.existsByCityAndDate("Moscow", localDateTime);
        assertTrue(result);
    }

    @Test
    public void existsByCityAndDateNoteExists () throws SQLException {
        LocalDate localDate = LocalDate.of()
        boolean result = weatherCityRepository.existsByCityAndDate("Moscow", "15.11.2022");
        assertFalse(result);
    }

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
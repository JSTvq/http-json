package com.kir138.task1.repository;

import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.sqlConnect.PgConnect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
public class WeatherCityRepositoryTest {

    @InjectMocks
    private WeatherCityRepository weatherCityRepository;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void saveNewCity() throws SQLException {
        CityDto cityDto = new CityDto(1L, "Moscow", "Russia", "20.20.2018",
                "Небольшая облачность", 15.0);

        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(resultSet.next()).thenReturn(false);
        CityDto result = weatherCityRepository.save(cityDto);
        assertNotNull(result);
        assertEquals(cityDto, result);
    }

    @Test
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
    public void existsByCityAndDateExists() throws SQLException {
        when(resultSet.next()).thenReturn(true);
        boolean result = weatherCityRepository.existsByCityAndDate("Moscow", "15.11.2022");
        assertTrue(result);
    }

    @Test
    public void existsByCityAndDateNoteExists() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        boolean result = weatherCityRepository.existsByCityAndDate("Moscow", "15.11.2022");
        assertFalse(result);
    }
}
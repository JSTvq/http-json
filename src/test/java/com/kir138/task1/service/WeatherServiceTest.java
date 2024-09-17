package com.kir138.task1.service;

import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.dto.LocationResponse;
import com.kir138.task1.repository.WeatherCityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private AccuWeatherClient accuWeatherClient;

    @Mock
    private WeatherCityRepository weatherCityRepository;

    @Mock
    private CustomCacheManager customCacheManager;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private WeatherHistoryMapper weatherHistoryMapper;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        // Инициализация происходит автоматически благодаря аннотациям
    }

    @Test
    void testRunExitImmediately() throws IOException {
        when(scanner.next()).thenReturn("exit");

        when(accuWeatherClient.getTopCities(150)).thenReturn(new LocationResponse[0]);

        weatherService.run();

        verify(accuWeatherClient).getTopCities(150);
        verifyNoMoreInteractions(accuWeatherClient, weatherCityRepository, customCacheManager, scanner);
    }

}

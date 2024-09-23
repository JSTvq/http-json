package com.kir138.task1.service;

import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.dto.LocationResponse;
import com.kir138.task1.repository.WeatherCityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

    @Spy
    private CustomCacheManager customCacheManager;

    @Mock
    private Scanner scanner;

    @Spy
    private WeatherHistoryMapper weatherHistoryMapper;

    @InjectMocks
    private WeatherService weatherService;

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(accuWeatherClient, weatherCityRepository, scanner,
                weatherHistoryMapper, customCacheManager);
    }

    @Test
    void testRunExitImmediately() throws IOException {
        when(scanner.next()).thenReturn("exit");
        when(accuWeatherClient.getTopCities(150))
                .thenReturn(new LocationResponse[0]);

        weatherService.run();

        verify(accuWeatherClient).getTopCities(150);
        verify(customCacheManager).getCache();
        verify(scanner).next();
    }
}

package com.kir138.task1.service;

import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.dto.LocationResponse;
import com.kir138.task1.model.dto.WeatherResponse;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.repository.WeatherCityRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        verifyNoMoreInteractions(accuWeatherClient, scanner,
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

    @Test
    public void runWithValidCity() throws IOException {

        LocationResponse city = new LocationResponse();
        city.setLocalizedName("Novosibirsk");

        WeatherResponse weatherResponse = getWeatherResponse();

        Map<Long, LocationResponse> cities = Map.of(1L, city);

        when(accuWeatherClient.getTopCities(150)).thenReturn(new LocationResponse[] {city});
        when(customCacheManager.getCache()).thenReturn(cities);
        when(scanner.next()).thenReturn("Novosibirsk", "exit");
        when(accuWeatherClient.getLocationKey("Novosibirsk")).thenReturn("88005553535");
        when(accuWeatherClient.getWeatherForecast("88005553535")).thenReturn(weatherResponse);
        when(weatherHistoryMapper.weatherForecast(weatherResponse, "Novosibirsk"))
                .thenReturn(Collections.singletonList(new WeatherHistory()));

        weatherService.run();

        verify(accuWeatherClient).getTopCities(150);
        verify(customCacheManager).updateCity(1L, city);
        verify(customCacheManager).getCache();
        verify(scanner, times(2)).next();
        verify(accuWeatherClient).getLocationKey("Novosibirsk");
        verify(accuWeatherClient).getWeatherForecast("88005553535");
        verify(weatherHistoryMapper).weatherForecast(weatherResponse, "Novosibirsk");
    }

    @Test
    public void runWithNonValidCity() throws IOException {
        LocationResponse city = new LocationResponse();
        city.setLocalizedName("Moscow");
        Map<Long, LocationResponse> listCity = Map.of(1L, city);

        when(accuWeatherClient.getTopCities(150)).thenReturn(new LocationResponse[]{city});
        when(customCacheManager.getCache()).thenReturn(listCity);
        when(scanner.next()).thenReturn("попробуйте ввести город еще раз", "exit");

        weatherService.run();

        verify(accuWeatherClient).getTopCities(150);
        verify(customCacheManager).updateCity(1L, city);
        verify(customCacheManager).getCache();
        verify(scanner, times(2)).next();
    }

    @NotNull
    private static WeatherResponse getWeatherResponse() {
        WeatherResponse.WeatherList.Value valueTemp = new WeatherResponse.WeatherList.Value();
        valueTemp.setTemp(15.0);

        WeatherResponse.WeatherList.Temperature temperature = new WeatherResponse.WeatherList.Temperature();
        temperature.setValue(valueTemp);

        WeatherResponse.WeatherList.Day day = new WeatherResponse.WeatherList.Day();
        day.setIconPhrase("Sunny");

        WeatherResponse.WeatherList weatherList = new WeatherResponse.WeatherList();
        weatherList.setDateText("2024-09-25T07:00:00-06:00");
        weatherList.setTemperature(temperature);
        weatherList.setDay(day);

        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setList(List.of(weatherList));
        return weatherResponse;
    }

    @Test
    public void findCityById() {
        Long id = 1L;
        WeatherHistory weatherHistory = new WeatherHistory();
        CityDto cityDto = new CityDto();

        when(weatherCityRepository.findById(id)).thenReturn(Optional.of(weatherHistory));
        when(weatherHistoryMapper.toCityDto(weatherHistory)).thenReturn(cityDto);

        CityDto actualCityDto = weatherService.findCityById(id);

        assertThat(actualCityDto).isNotNull().isEqualTo(cityDto);

        verify(weatherCityRepository).findById(id);
        verify(weatherHistoryMapper).toCityDto(weatherHistory);
    }

    @Test
    public void findCityByName() {
        //String nameCity = "Moscow";
        WeatherHistory weatherHistory1 = WeatherHistory.builder()
                .id(1L)
                .cityName("Moscow")
                .temperature(12.2)
                .weatherConditions("cold")
                .rqDateTime(LocalDate.of(2022, 11, 1))
                .build();
        /*List<WeatherHistory> weatherHistoryList = new ArrayList<>();
        CityDto cityDto = new CityDto();

        when(weatherCityRepository.findByNameCity(nameCity)).thenReturn((weatherHistoryList));
        when(weatherHistoryMapper.toCityDto((WeatherHistory) weatherHistoryList.stream()
                .map(weatherHistoryMapper::toCityDto)
                .toList()));

        WeatherHistory weatherHistory1

        List<CityDto> actualCityDto = weatherService.findCityByName(nameCity);

        assertThat(actualCityDto).isNotNull().containsExactlyInAnyOrder(cityDto);

        verify(weatherCityRepository).findByNameCity(nameCity);
        verify(weatherHistoryMapper).toCityDto((WeatherHistory) weatherHistoryList);*/
    }

    @Test
    public void findAllCities() {

    }

    @Test
    public void deleteCityById() {

    }

    @Test
    public void createTable() {

    }
}

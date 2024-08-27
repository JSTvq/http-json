package com.kir138.task1.service;

import com.kir138.task1.client.AccuWeatherClient;
import com.kir138.task1.model.dto.WeatherResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class TestService {
    private final AccuWeatherClient accuWeatherClient;

    public void test() throws IOException {
        WeatherResponse weatherForecast = accuWeatherClient.getWeatherForecast("123");
        List<WeatherResponse.WeatherList> list = weatherForecast.getList();
    }
}

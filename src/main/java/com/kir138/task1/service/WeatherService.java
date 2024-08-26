package com.kir138.task1.service;

import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.City;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.weatherInfo.WeatherResponse;
import lombok.Getter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class WeatherService {
    private final AccuWeatherClient accuWeatherClient;
    private String city;

    public WeatherService(AccuWeatherClient accuWeatherClient) {
        this.accuWeatherClient = accuWeatherClient;
    }

    WeatherCityRepository weatherCityRepository = new WeatherCityRepository();

    public void run() throws IOException {

        List<String> listCities = listCitiesCache();
        displayCityList(listCities);
        String citySelectedUser = getUserCityInput();

        if (!listCities.contains(citySelectedUser)) {
            System.out.println("Введенный город не найден. \nОбновите список городов и попробуйте снова");
            return;
        } else {
            city = citySelectedUser;
        }

        String locationKey = accuWeatherClient.getLocationKey(city);
        if (locationKey == null) {
            System.out.println("Введенный город не найден, обновите список городов");
            return;
        }

        WeatherResponse weatherResponse = accuWeatherClient.getWeatherForecast(locationKey);
        if (weatherResponse != null) {
            weatherCityRepository.save(weatherForecast(weatherResponse));
        }
    }

    private void displayCityList(List<String> listCities) {
        for (String city : listCities) {
            System.out.println(city);
        }
    }

    private String getUserCityInput() {
        return accuWeatherClient.inputCity();
    }

    public List<City> weatherForecast(WeatherResponse weatherResponse) {
        List<City> cityList = new ArrayList<>();
        for (WeatherResponse.WeatherList item : weatherResponse.getList()) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.getDateText());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String date = zonedDateTime.format(dateTimeFormatter);
            String icon = item.getDay().getIconPhrase();
            double temp = item.getTemperature().getValue().getTemp();
            System.out.println("В городе " + city + " на дату " + date + " ожидается: " + icon + ", " +
                    "температура " + temp + "°C");

            cityList.add(City.builder()
                    .cityName(city)
                    .date(date)
                    .weatherConditions(icon)
                    .temperature(temp)
                    .build());
        }
        return cityList;
    }

    public ArrayList<String> listCitiesCache() {
        CustomCacheManager customCacheManager = CustomCacheManager.getInstance();
        ArrayList<String> listCity = new ArrayList<>();
        try {
            List<City> cities = accuWeatherClient.getTopCities(150);
            for (City city : cities) {
                customCacheManager.putCity(city.getId(), city);
            }

            Map<Long, City> cache = customCacheManager.getCache();
            for (Map.Entry<Long, City> entry : cache.entrySet()) {
                String city = entry.getValue().getCountry(); // почему вместо getCountry выводятся города, а вместо getCityName страны??
                listCity.add(city);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listCity;
    }
}

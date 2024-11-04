package com.kir138.task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.AccuWeatherUrlBuilder;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.entity.City;
import com.kir138.task1.model.entity.User;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.repository.CityHibernateRepository;
import com.kir138.task1.repository.CrudRepository;
import com.kir138.task1.repository.WeatherCityHibernateRepository;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.service.HibernateWeatherService;
import com.kir138.task1.service.WeatherService;
import com.kir138.task1.sql.Connect.PgConnect;
import okhttp3.OkHttpClient;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        AccuWeatherUrlBuilder accuWeatherUrlBuilder = new AccuWeatherUrlBuilder();

        AccuWeatherClient accuWeatherClient = new AccuWeatherClient(okHttpClient, objectMapper, accuWeatherUrlBuilder);
        CustomCacheManager customCacheManager = new CustomCacheManager();
        SessionFactory sessionFactory = PgConnect.getSessionFactory();
        WeatherCityHibernateRepository weatherCityHibernateRepository = new WeatherCityHibernateRepository(sessionFactory);
        Connection connect = PgConnect.getConnection();



        CrudRepository<WeatherHistory, Long> weatherCityRepository = new WeatherCityRepository(connect);

        WeatherHistoryMapper weatherHistoryMapper = new WeatherHistoryMapper();

        HibernateWeatherService hibernateWeatherService = new HibernateWeatherService
                (weatherCityHibernateRepository, weatherHistoryMapper);
        Scanner scanner = new Scanner(System.in);
        CityHibernateRepository cityHibernateRepository = new CityHibernateRepository(sessionFactory, weatherCityHibernateRepository);
        WeatherService weatherService = new WeatherService(accuWeatherClient, weatherCityRepository, customCacheManager,
                weatherHistoryMapper, scanner, weatherCityHibernateRepository, cityHibernateRepository);

        //System.out.println("введите 1 если хотите запустить код через Hibernate, или 2 чтобы запустить код через JDBC");
        //int choiceBD = scanner.nextInt();
        if (true) {
            weatherCityRepository = new WeatherCityHibernateRepository(sessionFactory);
        } /*else if (choiceBD == 2) {
            weatherCityRepository = new WeatherCityRepository(connect);
            weatherService.createTable("weather");
        } else {
            throw new RuntimeException("нужная БД не найдена");
        }*/

        //weatherService.run();

        City city1 = City.builder()
                .cityName("Nicosia")
                .build()
                .withUser(User.builder()
                        .name("Nikolai")
                        .build())
                .withUser(User.builder()
                        .name("Artem")
                        .build());

        City city2 = City.builder()
                .cityName("Paramaribo")
                .build()
                .withUser(User.builder()
                        .name("Oleg")
                        .build())
                .withUser(User.builder()
                        .name("Kirill")
                        .build());

        City city3 = City.builder()
                .cityName("Novosibirsk")
                .build()
                .withUser(User.builder()
                        .name("Test1")
                        .build())
                .withUser(User.builder()
                        .name("Test2")
                        .build());

        cityHibernateRepository.saveCityIfExistsInWeatherHistory(city1);
        cityHibernateRepository.saveCityIfExistsInWeatherHistory(city2);
        cityHibernateRepository.saveCityIfExistsInWeatherHistory(city3);

        //System.out.println(weatherCityHibernateRepository.cityExistsInMainCity("Nicosia"));

        //System.out.println(hibernateWeatherService.findAllCities());
        //System.out.println(hibernateWeatherService.findCityById(2L));
        //System.out.println(hibernateWeatherService.findCityByName("Nicosia"));
        //hibernateWeatherService.deleteCityById(2L);
        //System.out.println(hibernateWeatherService.findAllCities());
    }
}

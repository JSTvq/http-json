package com.kir138.task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.AccuWeatherUrlBuilder;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.repository.CrudRepository;
import com.kir138.task1.repository.WeatherCityHibernateRepository;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.service.WeatherService;
import com.kir138.task1.sql.Connect.PgConnect;
import okhttp3.OkHttpClient;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.util.*;
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
        Scanner scanner = new Scanner(System.in);
        WeatherService weatherService = new WeatherService(accuWeatherClient, weatherCityRepository, customCacheManager,
                weatherHistoryMapper, scanner, weatherCityHibernateRepository);

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



        weatherService.run();


    }
}

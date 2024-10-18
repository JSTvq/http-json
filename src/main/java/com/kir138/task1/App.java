package com.kir138.task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kir138.task1.mapper.WeatherHistoryMapper;
import com.kir138.task1.model.AccuWeatherClient;
import com.kir138.task1.model.AccuWeatherUrlBuilder;
import com.kir138.task1.model.CustomCacheManager;
import com.kir138.task1.model.dto.CityDto;
import com.kir138.task1.model.entity.User;
import com.kir138.task1.model.entity.UserCityFavorites;
import com.kir138.task1.model.entity.WeatherHistory;
import com.kir138.task1.repository.CrudRepository;
import com.kir138.task1.repository.WeatherCityHibernateRepository;
import com.kir138.task1.repository.WeatherCityRepository;
import com.kir138.task1.service.WeatherService;
import com.kir138.task1.sql.Connect.PgConnect;
import okhttp3.OkHttpClient;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.SQLTimeoutException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.kir138.task1.sql.Connect.HibernateUtil.sessionFactory;

public class App {
    public static void main(String[] args) {

        final SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
        /**
         * Реализовать корректный вывод информации о текущей погоде. Создать класс WeatherResponse
         * и десериализовать ответ сервера. Выводить пользователю только текстовое описание
         * погоды и температуру в градусах цельсия.
         * Реализовать корректный выход из программы
         * Реализовать вывод погоды на следующие 5 дней в формате
         * В городе CITY на дату DATE ожидается WEATHER_TEXT, температура - TEMPERATURE
         * где CITY, DATE, WEATHER_TEXT и TEMPERATURE - уникальные значения для каждого дня.
         */

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        AccuWeatherUrlBuilder accuWeatherUrlBuilder = new AccuWeatherUrlBuilder();

        AccuWeatherClient accuWeatherClient = new AccuWeatherClient(okHttpClient, objectMapper, accuWeatherUrlBuilder);
        CustomCacheManager customCacheManager = new CustomCacheManager();
        Connection connect = PgConnect.getConnection();

        CrudRepository<WeatherHistory, Long> weatherCityRepository = new WeatherCityRepository(connect);

        WeatherHistoryMapper weatherHistoryMapper = new WeatherHistoryMapper();
        Scanner scanner = new Scanner(System.in);
        WeatherService weatherService = new WeatherService(accuWeatherClient, weatherCityRepository, customCacheManager,
                weatherHistoryMapper, scanner);

        System.out.println("введите 1 если хотите запустить код через Hibernate, или 2 чтобы запустить код через JDBC");
        int choiceBD = scanner.nextInt();
        if (choiceBD == 1) {
            weatherCityRepository = new WeatherCityHibernateRepository();
        } else if (choiceBD == 2) {
            weatherCityRepository = new WeatherCityRepository(connect);
            weatherService.createTable("weather");
        } else {
            throw new RuntimeException("нужная БД не найдена");
        }

        weatherService.run();
        UserCityFavorites userCityFavorites = new UserCityFavorites();
        //Set<UserCityFavorites> manyCities = new HashSet<>();
        List<UserCityFavorites> manyCities = new ArrayList<>();
        User user = User.builder()
                .name("Oleg")
                .build();

        userCityFavorites.setUser(user);

        WeatherHistory searchNameCity = weatherService.findCityByName("Male").stream()
                .map(weatherHistoryMapper::toWeatherHistory)
                .toList().get(0);

        //List<CityDto> searchNameCity = weatherService.str

        //WeatherHistory ss = searchNameCity.get(0).toWeatherHistory();

        userCityFavorites.setWeatherHistory(searchNameCity);

        System.out.println("абракакаокаок");
        System.out.println(weatherService.findCityByName("Moscow").get(0).getCityName());
        manyCities.add(userCityFavorites);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(user);
        session.persist(userCityFavorites);

        transaction.commit();

        session.close();
        sessionFactory.close();
    }
}

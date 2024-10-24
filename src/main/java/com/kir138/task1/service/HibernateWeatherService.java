package com.kir138.task1.service;

import com.kir138.task1.model.entity.City;
import com.kir138.task1.model.entity.WeatherHistory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateWeatherService {

    private static final SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();

    public void saveUniq(WeatherHistory weatherHistory) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                String cityName = weatherHistory.getCity().getCityName();
                City city = session.createQuery("FROM city WHERE city_name = :name", City.class)
                        .setParameter("city_name", cityName)
                        .uniqueResult();

                if (city == null) {
                    city = weatherHistory.getCity();
                    session.persist(city);
                }
                transaction.commit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

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

    public void saveUniq(String weatherHistory) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                String cityName = weatherHistory;
                System.out.println(cityName);

                WeatherHistory weatherHistory1 = session.createQuery("FROM weather WHERE city_name_history = :name", WeatherHistory.class)
                        .setParameter("city_name", cityName)
                        .uniqueResult();

                /*if (weatherHistory1 == null) {
                    session.persist(weatherHistory1);
                }*/

                session.persist(weatherHistory1);
                transaction.commit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

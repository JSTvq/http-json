package com.kir138.task1.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateWeatherService {

    private static final SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();


}

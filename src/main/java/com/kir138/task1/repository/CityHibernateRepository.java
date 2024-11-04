package com.kir138.task1.repository;

import com.kir138.task1.model.entity.City;
import com.kir138.task1.model.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CityHibernateRepository {

    private final SessionFactory sessionFactory;
    private final WeatherCityHibernateRepository weatherCityHibernateRepository;

    public CityHibernateRepository(SessionFactory sessionFactory, WeatherCityHibernateRepository weatherCityHibernateRepository) {
        this.sessionFactory = sessionFactory;
        this.weatherCityHibernateRepository = weatherCityHibernateRepository;
    }

    public City save(City city) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(city);
                transaction.commit();
                return city;
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("сохранение имени не произошло");
            }
        }
    }

    public void saveCityIfExistsInWeatherHistory(City city) {
        boolean exists = weatherCityHibernateRepository.cityExistsInMainCity(city.getCityName());
        if (!exists) {
            throw new IllegalArgumentException(city.getCityName() + " - такого города нет в weather");
        }
        if (!weatherCityHibernateRepository.cityExistsInCity(city.getCityName())) {
            save(city);
        } else {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    // Извлекаем существующий город из базы данных
                    City existingCity = session.createQuery("from City where cityName = :name", City.class)
                            .setParameter("name", city.getCityName())
                            .uniqueResult();

                    //System.out.println(existingCity);

                    if (existingCity != null) {
                        // Добавляем новых пользователей к существующему городу
                        for (User user : city.getUsers()) {
                            existingCity.withUser(user);
                            session.persist(user);
                        }
                        // Обновляем состояние города
                        session.merge(existingCity);
                    }

                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                    throw new RuntimeException("Не удалось добавить пользователей к существующему городу: " + e.getMessage());
                }
            }
        }
    }
}

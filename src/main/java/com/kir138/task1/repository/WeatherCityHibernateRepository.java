package com.kir138.task1.repository;

import com.kir138.task1.model.entity.City;
import com.kir138.task1.model.entity.WeatherHistory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WeatherCityHibernateRepository implements CrudRepository<WeatherHistory, Long> {

    private final SessionFactory sessionFactory;

    public WeatherCityHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<WeatherHistory> findAll() {
        try (Session session = sessionFactory.openSession()) {
            String fidAllCitiesQuery = "from weather";
            return session.createQuery(fidAllCitiesQuery, WeatherHistory.class).list();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<WeatherHistory> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            WeatherHistory weatherHistory = session.find(WeatherHistory.class, id);
            return Optional.ofNullable(weatherHistory);
        } catch (Exception e) {
            throw new RuntimeException("найти город по номеру не удалось");
        }
    }

    @Override
    public WeatherHistory save(WeatherHistory weatherHistory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(weatherHistory);
                transaction.commit();
                return weatherHistory;
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("сохранение/обновление не произошло");
            }
        }
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

    @Override
    public void deleteCityById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                WeatherHistory deleteWeatherHistory = session.get(WeatherHistory.class, id);
                session.remove(deleteWeatherHistory);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("удаление не произошло");
            }
        }
    }

    @Override
    public List<WeatherHistory> findByNameCity(String nameCity) {
        List<WeatherHistory> cities = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            WeatherHistory weatherHistory = session.find(WeatherHistory.class, nameCity);
            cities.add(weatherHistory);
            return cities;
        } catch (Exception e) {
            throw new RuntimeException("найти город по названию не удалось");
        }
    }

    public boolean cityExistsInMainCity(String cityName) {
        try (Session session = sessionFactory.openSession()) {
            Query<WeatherHistory> mainCityQuery = session.createQuery("from WeatherHistory where cityName = :name"
                    , WeatherHistory.class);
            mainCityQuery.setParameter("name", cityName);
            mainCityQuery.setMaxResults(1);
            return mainCityQuery.uniqueResult() != null;
        }
    }

    public boolean cityExistsInCity(String cityName) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> cityQuery = session.createQuery("from City where cityName = :name", City.class);
            cityQuery.setParameter("name", cityName);
            cityQuery.setMaxResults(1);
            return cityQuery.uniqueResult() != null;
        }
    }
}

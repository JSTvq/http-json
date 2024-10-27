package com.kir138.task1.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "city")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "city_name")
    private String cityName;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    @Builder.Default
    private List<User> users = new ArrayList<>();

    public City withUser(User user) {
        users.add(user);
        user.setCity(this);
        return this;
        /**
         * TODO
         * Переделать этот метод и поместить его куда-то в другое место
         * Нужно сделать так, чтобы я мог добавить юзера к какому-то городу из таблицы и убедиться, что такой город есть в БД,
         * если же города нет, то добавлять не нужно. Примерный функционал как в WeatherCityHibernateRepository который
         * я хочу добавить в WeatherService. Возможно это будут методы с дженериками чтобы туда можно было пихнуть и сити и юзера,
         * функционал вроде плюс минус такой же.
         */
    }
}

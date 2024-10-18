package com.kir138.task1.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/*@Getter
@Setter
@Entity
@Table(name = "userWeatherViews")
public class UserWeatherViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "weather_id", referencedColumnName = "id", nullable = false)
    private WeatherHistory weather;

    @Column(nullable = false)
    private LocalDateTime viewedAt;
}*/

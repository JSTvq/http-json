package com.kir138.task1.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name = "weather")
@Entity
public class WeatherHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city_name_history")
    private String cityName;

    @Column(name = "rq_date_time")
    private LocalDate rqDateTime;

    @Column(name = "weather_conditions")
    private String weatherConditions;

    @Column(name = "temperature")
    private Double temperature;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherHistory that)) return false;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}

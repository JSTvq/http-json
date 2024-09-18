package com.kir138.task1.model.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class WeatherHistory {
    private Long id;
    private String cityName;
    private LocalDate rqDateTime;
    private String weatherConditions;
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

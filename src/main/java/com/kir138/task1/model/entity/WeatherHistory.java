package com.kir138.task1.model.entity;

import lombok.*;

import java.time.LocalDate;

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
}

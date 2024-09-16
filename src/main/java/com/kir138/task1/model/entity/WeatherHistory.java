package com.kir138.task1.model.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class WeatherHistory {
    private Long id;
    private String cityName;
    private LocalDateTime rqDateTime;
    private String weatherConditions;
    private Double temperature;
}

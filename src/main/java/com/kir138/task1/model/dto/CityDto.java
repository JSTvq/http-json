package com.kir138.task1.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@ToString
public class CityDto {
    private final Long id;
    private final String cityName;
    private final String country;
    private final LocalDateTime date;
    private final String weatherConditions;
    private final Double temperature;
}

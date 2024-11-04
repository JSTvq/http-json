package com.kir138.task1.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@ToString
public class CityDto {
    private final Long id;
    private final String cityName;
    private final LocalDate date;
    private final String weatherConditions;
    private final Double temperature;
}

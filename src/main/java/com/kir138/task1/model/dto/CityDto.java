package com.kir138.task1.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class CityDto {
    private final Long id;
    private final String cityName;
    private final String country;
    private final String date;
    private final String weatherConditions;
    private final Double temperature;
}

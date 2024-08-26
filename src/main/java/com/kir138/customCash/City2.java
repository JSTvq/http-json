package com.kir138.customCash;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class City2 {
    private String id;
    private String cityName;
    private String country;

    // Конструкторы, гетеры и сетеры

    public City2() {}

    public City2(String id, String cityName, String country) {
        this.id = id;
        this.cityName = cityName;
        this.country = country;
    }
}
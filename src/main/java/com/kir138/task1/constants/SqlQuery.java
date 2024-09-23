package com.kir138.task1.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum SqlQuery {
    INSERT_CITY("INSERT INTO weather (city_name, rq_date_time, weather_conditions, temperature) "
            + "VALUES (?, ?, ?, ?)"),
    UNIQUE_CITY("SELECT 1 from weather where city_name = ? and rq_date_time = ?"),
    CREATE_TABLE("CREATE TABLE IF NOT EXISTS weather (" +
            "id SERIAL PRIMARY KEY," +
            "city_name VARCHAR(255) NOT NULL," +
            "rq_date_time TIMESTAMP NOT NULL," +
            "temperature DOUBLE PRECISION NOT NULL," +
            "weather_conditions VARCHAR(255) NOT NULL)"),
    FIND_CITY_NAME("SELECT * from weather where city_name = ?"),
    FIND_CITY_ID("SELECT * from weather where id = ?"),
    FIND_ALL_CITY("SELECT * from weather"),
    DELETE_CITY("DELETE from weather where id = ?");

    private final String query;
}

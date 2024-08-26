package com.kir138.task1.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum SqlQuery {
    INSERT_CITY("INSERT INTO weather (city, date, icon, temperature) "
            + "VALUES (?, ?, ?, ?)"),
    UNIQUE_CITY("SELECT 1 from weather where city = ? and date = ?");

    private final String query;
}

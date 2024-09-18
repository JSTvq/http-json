package com.kir138.task1.sql.Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgConnect {

    private final static String url = "jdbc:postgresql://localhost:5432/postgres";
    private final static String username = "admin";
    private final static String password = "admin";

    private static Connection CONNECTION;

    public static Connection getConnection() {
        try {
            if (CONNECTION == null) {
                CONNECTION = DriverManager.getConnection(url, username, password);
                CONNECTION.setAutoCommit(false);
                return CONNECTION;
            }
            return CONNECTION;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

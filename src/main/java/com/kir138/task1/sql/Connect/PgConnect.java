package com.kir138.task1.sql.Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgConnect {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String username = "postgres";
    private static final String password = "12341234";

    public static Connection getConnection() {
       try {
           Connection connection = DriverManager.getConnection(url, username, password);
           connection.setAutoCommit(false);
           return connection;
       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    public static Connection getConenctionTest() {
        try {
            String testUrl = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(testUrl, username, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

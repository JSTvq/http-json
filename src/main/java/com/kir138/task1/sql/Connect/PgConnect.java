package com.kir138.task1.sql.Connect;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

    public static SessionFactory getSessionFactory() {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }
}

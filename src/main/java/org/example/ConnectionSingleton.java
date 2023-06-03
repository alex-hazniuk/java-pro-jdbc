package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {
    private static final String URL = "jdbc:mysql://localhost:3306/questionarium";
    private static final String NAME = "root";
    private static final String PASSWORD = "jewelleralex";
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, NAME, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't create connection", e);
        }
        return connection;
    }
}

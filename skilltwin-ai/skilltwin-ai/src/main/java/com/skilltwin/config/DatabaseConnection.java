package com.skilltwin.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/skilltwin_db";
    private static final String USER = "root";
    public static Connection getConnection()
            throws SQLException {
        String password = System.getenv("DB_PASSWORD")
        if (password == null || password.isBlank()) {
            throw new SQLException("DB_PASSWORD environment variable is not set.");
        }
        return DriverManager.getConnection(URL, USER, password);
    }
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println(
                    "Connected to SkillTwin AI database successfully!"
            );
            System.out.println(
                    "Database: " +
                    connection.getCatalog()
            );
        } 
        catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}

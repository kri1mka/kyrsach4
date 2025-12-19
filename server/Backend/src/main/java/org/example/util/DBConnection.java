package org.example.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC Driver not found");
        }
    }

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            Properties props = new Properties();
            try (InputStream is = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream(PROPERTIES_FILE)) {

                if (is == null) {
                    throw new RuntimeException("db.properties not found in classpath");
                }
                props.load(is);
            }

            Connection connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );

            if (!connection.isValid(2)) {
                throw new SQLException("Connection is not valid");
            }

            System.out.println("DB connection established successfully");
            return connection;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create DB connection", e);
        }
    }
}
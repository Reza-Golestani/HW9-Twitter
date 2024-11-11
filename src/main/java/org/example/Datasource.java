package org.example;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {

    @Getter
    private static final Connection connection;

    private Datasource() {
    }

    static {
        var jdbc = "jdbc:postgresql://localhost:5432/HW9-Twitter";

        try {
            connection = DriverManager.getConnection(jdbc, "postgres", "postgres");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // JVM Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

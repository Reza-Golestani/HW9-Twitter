package org.example.repository;

import org.example.Datasource;

import java.sql.SQLException;

public class TagRepository {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tags (
            id bigserial PRIMARY KEY NOT NULL,
            name varchar(50) UNIQUE NOT NULL
            );
            """;

    public static void initTable() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CREATE_TABLE);
        statement.execute();
        statement.close();
    }
}

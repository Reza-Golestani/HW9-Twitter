package org.example.repository;

import org.example.Datasource;

import java.sql.SQLException;

public class ReactionRepository {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS reactions (
            id bigserial PRIMARY KEY NOT NULL,
            tweet_id bigint NOT NULL,
            user_id bigint NOT NULL,
            reaction_type varchar(30) NOT NULL,
            created_at timestamp NOT NULL,
            FOREIGN KEY (tweet_id) REFERENCES tweets,
            FOREIGN KEY (user_id) REFERENCES users
            );
            """;

    public static void initTable() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CREATE_TABLE);
        statement.execute();
        statement.close();
    }
}

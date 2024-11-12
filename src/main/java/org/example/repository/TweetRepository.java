package org.example.repository;

import org.example.Datasource;
import org.example.entity.Tweet;
import org.example.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TweetRepository {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tweets (
            id bigserial PRIMARY KEY NOT NULL,
            text varchar(280) NOT NULL,
            user_id bigint NOT NULL,
            created_at timestamp NOT NULL,
            updated_at timestamp,
            FOREIGN KEY (user_id) REFERENCES users
            );
            """;

    public static void initTable() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CREATE_TABLE);
        statement.execute();
        statement.close();
    }

      private static String INSERT_TWEET = """
                INSERT INTO tweets (text, user_id, created_at)
                VALUES (?, ?, ?)
                RETURNING id
                """;

    public static Tweet save(Tweet newTweet) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(INSERT_TWEET);
        statement.setString(1, newTweet.getText());
        statement.setLong(2, UserService.loggedInUser.getId());
        statement.setTimestamp(3, Timestamp.valueOf(newTweet.getCreatedAt()));

        try(ResultSet resultSet = statement.executeQuery()){
            if (resultSet.next()){
                newTweet.setId(resultSet.getLong("id"));
            }
            statement.close();
            return newTweet;
        }
    }

}

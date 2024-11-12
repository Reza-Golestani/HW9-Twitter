package org.example.repository;

import org.example.Datasource;

import java.sql.SQLException;

public class Tweet_TagRepository {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tweets_tags (
            id bigserial PRIMARY KEY NOT NULL,
            tweet_id bigint NOT NULL,
            tag_id bigint NOT NULL,
            FOREIGN KEY (tweet_id) REFERENCES tweets,
            FOREIGN KEY (tag_id) REFERENCES tags
            );
            """;

    public static void initTable() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CREATE_TABLE);
        statement.execute();
        statement.close();
    }

    private static String INSERT_TWEET_TAG = """
            INSERT INTO tweets_tags (tweet_id, tag_id)
            VALUES (? , ?)
            """;

    public static void save(long TweetId, long tagId) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(INSERT_TWEET_TAG);
        statement.setLong(1, TweetId);
        statement.setLong(2, tagId);
        statement.execute();
        statement.close();
    }
}

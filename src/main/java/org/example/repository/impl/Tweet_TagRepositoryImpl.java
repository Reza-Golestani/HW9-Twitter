package org.example.repository.impl;

import org.example.Datasource;
import org.example.entity.Tweet;
import org.example.repository.Twee_TagRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tweet_TagRepositoryImpl implements Twee_TagRepository {

    private final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tweets_tags (
            id bigserial PRIMARY KEY NOT NULL,
            tweet_id bigint NOT NULL,
            tag_id bigint NOT NULL,
            FOREIGN KEY (tweet_id) REFERENCES tweets,
            FOREIGN KEY (tag_id) REFERENCES tags
            );
            """;

    public void initTable() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CREATE_TABLE);
        statement.execute();
        statement.close();
    }

    private final String INSERT_TWEET_TAG = """
            INSERT INTO tweets_tags (tweet_id, tag_id)
            VALUES (? , ?)
            """;

    public void save(long TweetId, long tagId) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(INSERT_TWEET_TAG);
        statement.setLong(1, TweetId);
        statement.setLong(2, tagId);
        statement.execute();
        statement.close();
    }

    private final String DELETE_BY_TWEET_ID = """
            DELETE FROM tweets_tags
            WHERE tweet_id = ?
            """;

    public void deleteByTweet(Tweet tweet) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(DELETE_BY_TWEET_ID);
        statement.setLong(1, tweet.getId());
        statement.execute();
        statement.close();
    }

    private final String IS_TAG_USELESS = """
            SELECT * FROM tweets_tags
            WHERE tag_id = ? and tweet_id <> ?
            """;

    public boolean isTagUseless(long tweetId, long tagId) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(IS_TAG_USELESS);
        statement.setLong(1, tagId);
        statement.setLong(2, tweetId);
        try(ResultSet resultSet = statement.executeQuery()) {
            return !resultSet.next();
        }
    }

}

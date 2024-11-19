package org.example.repository;

import org.example.Datasource;
import org.example.entity.Tweet;

import java.sql.ResultSet;
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

    public static String DELETE_BY_TWEET_ID = "DELETE FROM reactions WHERE tweet_id = ?";

    public static void deleteByTweet(Tweet tweet) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(DELETE_BY_TWEET_ID);
        statement.setLong(1, tweet.getId());
        statement.execute();
        statement.close();
    }

    public static String LIKES_COUNT = """
            SELECT COUNT(tweet_id) FROM reactions
            WHERE tweet_id =? AND reaction_type = 'LIKE'
            """;

    public static String DISLIKES_COUNT = """
            SELECT COUNT(tweet_id) FROM reactions
            WHERE tweet_id =? AND reaction_type = 'DISLIKE'
            """;

    public static String RETWEETS_COUNT = """
            SELECT COUNT(retweeted) FROM tweets
            WHERE retweeted =?
            """;

    public static long likesCount(long tweetId) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(LIKES_COUNT);
        statement.setLong(1, tweetId);
        try (ResultSet resultSet = statement.executeQuery();) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return 0;
    }

    public static long dislikesCount(long tweetId) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(DISLIKES_COUNT);
        statement.setLong(1, tweetId);
        try (ResultSet resultSet = statement.executeQuery();) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return 0;
    }

    public static long retweetCount(long tweetId) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(RETWEETS_COUNT);
        statement.setLong(1, tweetId);
        try (ResultSet resultSet = statement.executeQuery();) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return 0;
    }


}

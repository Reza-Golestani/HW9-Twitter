package org.example.repository;

import org.example.Datasource;
import org.example.entity.Tweet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    public static String FIND_REACTION = """
                SELECT reaction_type FROM reactions
                WHERE user_id =? AND tweet_id =?
                """;


    public static String currentReaction(long userId, long tweetId) throws SQLException {

        var statement = Datasource.getConnection().prepareStatement(FIND_REACTION);
        statement.setLong(1, userId);
        statement.setLong(2, tweetId);
        try(ResultSet resultSet = statement.executeQuery()){
            if(resultSet.next()){
                return resultSet.getString("reaction_type");
            } else return "None!";
        }

    }

    public static String INSERT_LIKE = """
            INSERT INTO  reactions (tweet_id, user_id, reaction_type, created_at) VALUES (?,?,'LIKE',?)
            """;

    public static String INSERT_DISLIKE = """
            INSERT INTO  reactions (tweet_id, user_id, reaction_type, created_at) VALUES (?,?,'DISLIKE',?)            """;

    public static String UPDATE_TO_LIKE = """
            UPDATE reactions SET reaction_type = 'LIKE' WHERE tweet_id = ? AND user_id = ?
            """;

    public static String UPDATE_TO_DISLIKE = """
            UPDATE reactions SET reaction_type = 'DISLIKE' WHERE tweet_id = ? AND user_id = ?
            """;

    public static String CLEAR_REACTION = """
            DELETE FROM reactions WHERE tweet_id = ? AND user_id = ?
            """;

    public static void like(long userId, long tweetId) throws SQLException {

        var findStatement = Datasource.getConnection().prepareStatement(FIND_REACTION);
        findStatement.setLong(1, userId);
        findStatement.setLong(2, tweetId);
        try(ResultSet resultSet = findStatement.executeQuery()){
            if(resultSet.next()){
                var updateStatement = Datasource.getConnection().prepareStatement(UPDATE_TO_LIKE);
                updateStatement.setLong(1, tweetId);
                updateStatement.setLong(2, userId);
                updateStatement.executeUpdate();
                updateStatement.close();
            } else {
                var insertStatement = Datasource.getConnection().prepareStatement(INSERT_LIKE);
                insertStatement.setLong(2, userId);
                insertStatement.setLong(1, tweetId);
                insertStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                insertStatement.executeUpdate();
                insertStatement.close();
            }
        }
    }

    public static void dislike(long userId, long tweetId) throws SQLException {

        var findStatement = Datasource.getConnection().prepareStatement(FIND_REACTION);
        findStatement.setLong(1, userId);
        findStatement.setLong(2, tweetId);
        try(ResultSet resultSet = findStatement.executeQuery()){
            if(resultSet.next()){
                var updateStatement =
                        Datasource.getConnection().prepareStatement(UPDATE_TO_DISLIKE);
                updateStatement.setLong(1, tweetId);
                updateStatement.setLong(2, userId);
                updateStatement.executeUpdate();
                updateStatement.close();
            } else {
                var insertStatement = Datasource.getConnection().prepareStatement(INSERT_DISLIKE);
                insertStatement.setLong(2, userId);
                insertStatement.setLong(1, tweetId);
                insertStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                insertStatement.executeUpdate();
                insertStatement.close();
            }
        }
    }

    public static void clearReaction(long userId, long tweetId) throws SQLException {
        var findStatement = Datasource.getConnection().prepareStatement(FIND_REACTION);
        findStatement.setLong(1, userId);
        findStatement.setLong(2, tweetId);
        try(ResultSet resultSet = findStatement.executeQuery()){
            if(resultSet.next()){
                var clearStatement = Datasource.getConnection().prepareStatement(CLEAR_REACTION);
                clearStatement.setLong(2, userId);
                clearStatement.setLong(1, tweetId);
                clearStatement.executeUpdate();
                clearStatement.close();
            }
        }
    }

}

package org.example.repository;

import org.example.Datasource;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;

public class TweetRepository {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tweets (
            id bigserial PRIMARY KEY NOT NULL,
            text varchar(280) NOT NULL,
            user_id bigint NOT NULL,
            created_at timestamp NOT NULL,
            updated_at timestamp,
            retweeted bigint,
            FOREIGN KEY (user_id) REFERENCES users,
            FOREIGN KEY (retweeted) REFERENCES tweets
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

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                newTweet.setId(resultSet.getLong("id"));
            }
            statement.close();
            return newTweet;
        }
    }

    public static String DELETE_TWEET_BY_ID = """
            DELETE from tweets
            WHERE id = ?
            """;

    public static void delete(Tweet tweet) throws SQLException {
        long id = tweet.getId();
        var statement = Datasource.getConnection().prepareStatement(DELETE_TWEET_BY_ID);
        statement.setLong(1, id);
        statement.execute();
        statement.close();
    }

    public static String GET_ALL = """
            SELECT * FROM tweets JOIN users ON tweets.user_id = users.id
            """;

    public static String GET_TAGS_NAMES = """
            SELECT name FROM tweets_tags JOIN tags ON tweets_tags.tag_id = tags.id
            WHERE tweets_tags.tweet_id = ?
            """;

    public static HashSet<String> getTags(long tweetId) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(GET_TAGS_NAMES);
        statement.setLong(1, tweetId);
        try (ResultSet resultSet = statement.executeQuery()) {
            HashSet<String> tags = new HashSet<>();
            while (resultSet.next()) {
                tags.add(resultSet.getString("name"));
            }
            return tags;
        }
    }

    public static ArrayList<Tweet> getAllTweets() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(GET_ALL);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Tweet> tweets = new ArrayList<>();
        while (resultSet.next()) {
            Tweet tweet = new Tweet();
            User writer = new User();
            writer.setId(resultSet.getLong("user_id"));
            writer.setDisplayedName(resultSet.getString("displayed_name"));
            tweet.setWriter(writer);
            tweet.setId(resultSet.getLong("id"));
            tweet.setText(resultSet.getString("text"));
            tweet.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            if (resultSet.getTimestamp("updated_at") != null)
                tweet.setEditedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
            tweet.setTags(getTags(tweet.getId()));
            tweets.add(tweet);
        }
        statement.close();
        return tweets;
    }

    public static String GET_ALL_YOURS = """
            SELECT * FROM tweets JOIN users ON tweets.user_id = users.id
            WHERE user_id = ?
            """;

    public static ArrayList<Tweet> getAllTweets(User user) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(GET_ALL_YOURS);
        statement.setLong(1, user.getId());
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Tweet> tweets = new ArrayList<>();
        while (resultSet.next()) {
            Tweet tweet = new Tweet();
            tweet.setWriter(user);
            tweet.setId(resultSet.getLong("id"));
            tweet.setText(resultSet.getString("text"));
            tweet.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            tweet.setTags(getTags(tweet.getId()));
            tweets.add(tweet);
        }
        statement.close();
        return tweets;
    }
}

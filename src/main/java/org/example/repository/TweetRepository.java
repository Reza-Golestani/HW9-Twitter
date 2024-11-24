package org.example.repository;

import org.example.entity.Tweet;
import org.example.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public interface TweetRepository {

    void initTable() throws SQLException;

    Tweet save(Tweet newTweet) throws SQLException;

    void delete(Tweet tweet) throws SQLException;

    HashSet<String> getTags(long tweetId) throws SQLException;

    Tweet getOne(long tweetId) throws SQLException;

    ArrayList<Tweet> getAllTweets() throws SQLException;

    ArrayList<Tweet> getAllTweets(User user) throws SQLException;

    void editText(long tweetId, String newText) throws SQLException;

    void updatedAt(long tweetId) throws SQLException;

    void setRetweeted(long tweetId, long retweetedId) throws SQLException;

    void handleDeleteReference(long referenceId) throws SQLException;

}

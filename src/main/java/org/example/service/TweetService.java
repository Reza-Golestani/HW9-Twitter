package org.example.service;

import org.example.entity.Tweet;
import org.example.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

public interface TweetService {

    Tweet create(String newTweetText, Set<String> tagTitles);

    void deleteTweet(Tweet tweet) throws SQLException;

    void handleDeleteReference(Tweet tweet) throws SQLException;

    ArrayList<Tweet> getAll() throws SQLException;

    ArrayList<Tweet> getAll(User user) throws SQLException;

    void editTags(Tweet tweet, Set<String> newTagTitles) throws SQLException;

    void editText(Tweet tweet, String newText) throws SQLException;

    void setRetweeted(long tweetId, long retweetedId) throws SQLException;

    long save(Tweet newTweet) throws SQLException;

    void tweetLengthCheck(long tweetLength);

}

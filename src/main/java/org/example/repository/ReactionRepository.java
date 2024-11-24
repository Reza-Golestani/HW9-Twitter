package org.example.repository;

import org.example.entity.Tweet;

import java.sql.SQLException;

public interface ReactionRepository {

    void initTable() throws SQLException;

    void deleteByTweet(Tweet tweet) throws SQLException;

    long likesCount(long tweetId) throws SQLException;

    long dislikesCount(long tweetId) throws SQLException;

    long retweetCount(long tweetId) throws SQLException;

    String currentReaction(long userId, long tweetId) throws SQLException;

    void like(long userId, long tweetId) throws SQLException;

    void dislike(long userId, long tweetId) throws SQLException;

    void clearReaction(long userId, long tweetId) throws SQLException;

}

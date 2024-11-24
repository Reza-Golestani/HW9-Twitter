package org.example.service;

import java.sql.SQLException;

public interface ReactionService {

    long reactionsCount(long tweetId, String reaction) throws SQLException;

    String currentReaction(long userId, long tweetId) throws SQLException;

    void like(long userId, long tweetId) throws SQLException;

    void dislike(long userId, long tweetId) throws SQLException;

    void clearReaction(long userId, long tweetId) throws SQLException;

}

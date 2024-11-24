package org.example.service.impl;

import org.example.repository.impl.ReactionRepositoryImpl;

import java.sql.SQLException;

public class ReactionServiceImpl {

    public static long reactionsCount(long tweetId, String reaction) throws SQLException {
        return switch (reaction) {
            case "like" -> ReactionRepositoryImpl.likesCount(tweetId);
            case "dislike" -> ReactionRepositoryImpl.dislikesCount(tweetId);
            case "retweet" -> ReactionRepositoryImpl.retweetCount(tweetId);
            default -> 0;
        };
    }

    public static String currentReaction(long userId, long tweetId) throws SQLException {
        return ReactionRepositoryImpl.currentReaction(userId, tweetId);
    }

    public static void like(long userId, long tweetId) throws SQLException {
        ReactionRepositoryImpl.like(userId, tweetId);
    }

    public static void dislike(long userId, long tweetId) throws SQLException {
        ReactionRepositoryImpl.dislike(userId, tweetId);
    }

    public static void clearReaction(long userId, long tweetId) throws SQLException {
        ReactionRepositoryImpl.clearReaction(userId, tweetId);
    }

}

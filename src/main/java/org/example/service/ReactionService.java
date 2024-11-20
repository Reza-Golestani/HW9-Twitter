package org.example.service;

import org.example.repository.ReactionRepository;

import java.sql.SQLException;

public class ReactionService {

    public static long reactionsCount(long tweetId, String reaction) throws SQLException {
        return switch (reaction) {
            case "like" -> ReactionRepository.likesCount(tweetId);
            case "dislike" -> ReactionRepository.dislikesCount(tweetId);
            case "retweet" -> ReactionRepository.retweetCount(tweetId);
            default -> 0;
        };
    }

    public static String currentReaction(long userId, long tweetId) throws SQLException {
        return ReactionRepository.currentReaction(userId, tweetId);
    }

    public static void like(long userId, long tweetId) throws SQLException {
        ReactionRepository.like(userId, tweetId);
    }

    public static void dislike(long userId, long tweetId) throws SQLException {
        ReactionRepository.dislike(userId, tweetId);
    }

    public static void clearReaction(long userId, long tweetId) throws SQLException {
        ReactionRepository.clearReaction(userId, tweetId);
    }

}

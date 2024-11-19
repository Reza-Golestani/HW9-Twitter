package org.example.service;

import org.example.repository.ReactionRepository;

import java.sql.SQLException;

public class ReactionService {

    public static long reactionsCount(long tweetId, String reaction) throws SQLException {
        long count = 0;
        switch (reaction) {
            case "like":
                count = ReactionRepository.likesCount(tweetId);
                break;
            case "dislike":
                count = ReactionRepository.dislikesCount(tweetId);
                break;
            case "retweet":
                count = ReactionRepository.retweetCount(tweetId);
                break;
        }
        return count;
    }
}

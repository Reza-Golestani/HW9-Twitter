package org.example.service.impl;

import org.example.repository.impl.ReactionRepositoryImpl;
import org.example.service.ReactionService;

import java.sql.SQLException;

public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepositoryImpl reactionRepositoryImpl = new ReactionRepositoryImpl();

    @Override
    public long reactionsCount(long tweetId, String reaction) throws SQLException {
        return switch (reaction) {
            case "like" -> reactionRepositoryImpl.likesCount(tweetId);
            case "dislike" -> reactionRepositoryImpl.dislikesCount(tweetId);
            case "retweet" -> reactionRepositoryImpl.retweetCount(tweetId);
            default -> 0;
        };
    }

    @Override
    public String currentReaction(long userId, long tweetId) throws SQLException {
        return reactionRepositoryImpl.currentReaction(userId, tweetId);
    }

    @Override
    public void like(long userId, long tweetId) throws SQLException {
        reactionRepositoryImpl.like(userId, tweetId);
    }

    @Override
    public void dislike(long userId, long tweetId) throws SQLException {
        reactionRepositoryImpl.dislike(userId, tweetId);
    }

    @Override
    public void clearReaction(long userId, long tweetId) throws SQLException {
        reactionRepositoryImpl.clearReaction(userId, tweetId);
    }

}

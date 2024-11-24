package org.example.repository;

import org.example.entity.Tweet;

import java.sql.SQLException;

public interface Twee_TagRepository {

    void initTable() throws SQLException;

    void save(long TweetId, long tagId) throws SQLException;

    void deleteByTweet(Tweet tweet) throws SQLException;

    boolean isTagUseless(long tweetId, long tagId) throws SQLException;

}

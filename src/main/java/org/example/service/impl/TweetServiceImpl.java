package org.example.service.impl;

import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.repository.impl.ReactionRepositoryImpl;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.repository.impl.TweetRepositoryImpl;
import org.example.repository.impl.Tweet_TagRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public class TweetServiceImpl {

    public static Tweet create(String newTweetText, Set<String> tagTitles) {
        Tweet newTweet = new Tweet();
        newTweet.setText(newTweetText);
        newTweet.setTags(tagTitles);
        newTweet.setCreatedAt(LocalDateTime.now());
        newTweet.setWriter(UserServiceImpl.loggedInUser);
        return newTweet;
    }

    public static void deleteTweet(Tweet tweet) throws SQLException {

        ReactionRepositoryImpl.deleteByTweet(tweet);
        Tweet_TagRepositoryImpl.deleteByTweet(tweet);
        if (tweet.getTags() != null) {
            for (String tag : tweet.getTags()) {
                if (Tweet_TagRepositoryImpl.isTagUseless(tweet.getId(), TagRepositoryImpl.getTagId(tag))) {
                    TagRepositoryImpl.delete(tag);
                }
            }
        }
        handleDeleteReference(tweet);
        TweetRepositoryImpl.delete(tweet);
    }

    private static void handleDeleteReference(Tweet tweet) throws SQLException {
        TweetRepositoryImpl.handleDeleteReference(tweet.getId());
    }

    public static ArrayList<Tweet> getAll() throws SQLException {
        return TweetRepositoryImpl.getAllTweets();
    }

    public static ArrayList<Tweet> getAll(User user) throws SQLException {
        return TweetRepositoryImpl.getAllTweets(user);
    }


    public static void editTags(Tweet tweet, Set<String> newTagTitles) throws SQLException {
        Tweet_TagRepositoryImpl.deleteByTweet(tweet);
        if (tweet.getTags() != null) {
            for (String tag : tweet.getTags()) {
                if (Tweet_TagRepositoryImpl.isTagUseless(tweet.getId(), TagRepositoryImpl.getTagId(tag))) {
                    TagRepositoryImpl.delete(tag);
                }
            }
        }
        tweet.setTags(newTagTitles);
        TagServiceImpl.saveTags(tweet);
        TweetRepositoryImpl.updatedAt(tweet.getId());
        tweet.setEditedAt(LocalDateTime.now());
    }

    public static void editText(Tweet tweet, String newText) throws SQLException {
        TweetRepositoryImpl.editText(tweet.getId(), newText);
        TweetRepositoryImpl.updatedAt(tweet.getId());
        tweet.setEditedAt(LocalDateTime.now());
    }

    public static void setRetweeted(long tweetId, long retweetedId) throws SQLException {
        TweetRepositoryImpl.setRetweeted(tweetId, retweetedId);
    }

    public static long save(Tweet newTweet) throws SQLException {
        return TweetRepositoryImpl.save(newTweet).getId();
    }
}

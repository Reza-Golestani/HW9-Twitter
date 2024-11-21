package org.example.service;

import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.repository.ReactionRepository;
import org.example.repository.TagRepository;
import org.example.repository.TweetRepository;
import org.example.repository.Tweet_TagRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public class TweetService {

    public static Tweet create(String newTweetText, Set<String> tagTitles) {
        Tweet newTweet = new Tweet();
        newTweet.setText(newTweetText);
        newTweet.setTags(tagTitles);
        newTweet.setCreatedAt(LocalDateTime.now());
        newTweet.setWriter(UserService.loggedInUser);
        return newTweet;
    }

    public static void deleteTweet(Tweet tweet) throws SQLException {

        ReactionRepository.deleteByTweet(tweet);
        Tweet_TagRepository.deleteByTweet(tweet);
        if (tweet.getTags() != null) {
            for (String tag : tweet.getTags()) {
                if (Tweet_TagRepository.isTagUseless(tweet.getId(), TagRepository.getTagId(tag))) {
                    TagRepository.delete(tag);
                }
            }
        }
        handleDeleteReference(tweet);
        TweetRepository.delete(tweet);
    }

    private static void handleDeleteReference(Tweet tweet) throws SQLException {
        TweetRepository.handleDeleteReference(tweet.getId());
    }

    public static ArrayList<Tweet> getAll() throws SQLException {
        return TweetRepository.getAllTweets();
    }

    public static ArrayList<Tweet> getAll(User user) throws SQLException {
        return TweetRepository.getAllTweets(user);
    }


    public static void editTags(Tweet tweet, Set<String> newTagTitles) throws SQLException {
        Tweet_TagRepository.deleteByTweet(tweet);
        if (tweet.getTags() != null) {
            for (String tag : tweet.getTags()) {
                if (Tweet_TagRepository.isTagUseless(tweet.getId(), TagRepository.getTagId(tag))) {
                    TagRepository.delete(tag);
                }
            }
        }
        tweet.setTags(newTagTitles);
        TagService.saveTags(tweet);
        TweetRepository.updatedAt(tweet.getId());
        tweet.setEditedAt(LocalDateTime.now());
    }

    public static void editText(Tweet tweet, String newText) throws SQLException {
        TweetRepository.editText(tweet.getId(), newText);
        TweetRepository.updatedAt(tweet.getId());
        tweet.setEditedAt(LocalDateTime.now());
    }

    public static void setRetweeted(long tweetId, long retweetedId) throws SQLException {
        TweetRepository.setRetweeted(tweetId, retweetedId);
    }
}

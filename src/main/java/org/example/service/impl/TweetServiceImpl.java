package org.example.service.impl;

import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.repository.impl.ReactionRepositoryImpl;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.repository.impl.TweetRepositoryImpl;
import org.example.repository.impl.Tweet_TagRepositoryImpl;
import org.example.service.TweetService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

public class TweetServiceImpl implements TweetService {

    private ReactionRepositoryImpl reactionRepositoryImpl = new ReactionRepositoryImpl();
    private Tweet_TagRepositoryImpl tweet_tagRepositoryImpl = new Tweet_TagRepositoryImpl();
    private TagRepositoryImpl tagRepositoryImpl = new TagRepositoryImpl();
    private TweetRepositoryImpl tweetRepositoryImpl = new TweetRepositoryImpl();
    private TagServiceImpl tagServiceImpl = new TagServiceImpl();

    public Tweet create(String newTweetText, Set<String> tagTitles) {
        Tweet newTweet = new Tweet();
        newTweet.setText(newTweetText);
        newTweet.setTags(tagTitles);
        newTweet.setCreatedAt(LocalDateTime.now());
        newTweet.setWriter(UserServiceImpl.loggedInUser);
        return newTweet;
    }

    public void deleteTweet(Tweet tweet) throws SQLException {

        reactionRepositoryImpl.deleteByTweet(tweet);
        tweet_tagRepositoryImpl.deleteByTweet(tweet);
        if (tweet.getTags() != null) {
            for (String tag : tweet.getTags()) {
                if (tweet_tagRepositoryImpl.isTagUseless(tweet.getId(),
                        tagRepositoryImpl.getTagId(tag))) {
                    tagRepositoryImpl.delete(tag);
                }
            }
        }
        handleDeleteReference(tweet);
        tweetRepositoryImpl.delete(tweet);
    }

    private void handleDeleteReference(Tweet tweet) throws SQLException {
        tweetRepositoryImpl.handleDeleteReference(tweet.getId());
    }

    public ArrayList<Tweet> getAll() throws SQLException {
        return tweetRepositoryImpl.getAllTweets();
    }

    public ArrayList<Tweet> getAll(User user) throws SQLException {
        return tweetRepositoryImpl.getAllTweets(user);
    }


    public void editTags(Tweet tweet, Set<String> newTagTitles) throws SQLException {
        tweet_tagRepositoryImpl.deleteByTweet(tweet);
        if (tweet.getTags() != null) {
            for (String tag : tweet.getTags()) {
                if (tweet_tagRepositoryImpl.isTagUseless(tweet.getId(),
                        tagRepositoryImpl.getTagId(tag))) {
                    tagRepositoryImpl.delete(tag);
                }
            }
        }
        tweet.setTags(newTagTitles);
        tagServiceImpl.saveTags(tweet);
        tweetRepositoryImpl.updatedAt(tweet.getId());
        tweet.setEditedAt(LocalDateTime.now());
    }

    public void editText(Tweet tweet, String newText) throws SQLException {
        tweetRepositoryImpl.editText(tweet.getId(), newText);
        tweetRepositoryImpl.updatedAt(tweet.getId());
        tweet.setEditedAt(LocalDateTime.now());
    }

    public void setRetweeted(long tweetId, long retweetedId) throws SQLException {
        tweetRepositoryImpl.setRetweeted(tweetId, retweetedId);
    }

    public long save(Tweet newTweet) throws SQLException {
        return tweetRepositoryImpl.save(newTweet).getId();
    }
}

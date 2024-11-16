package org.example.service;

import org.example.entity.Tweet;
import org.example.repository.ReactionRepository;
import org.example.repository.TagRepository;
import org.example.repository.TweetRepository;
import org.example.repository.Tweet_TagRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
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

        TweetRepository.delete(tweet);
        ReactionRepository.deleteByTweet(tweet);
        Tweet_TagRepository.deleteByTweet(tweet);
        if (tweet.getTags() != null) {
            for (String tag : tweet.getTags()) {
                if (Tweet_TagRepository.isTagUseless(tweet.getId(), TagRepository.getTagId(tag))) {
                    TagRepository.delete(tag);
                }
            }
        }
    }
}

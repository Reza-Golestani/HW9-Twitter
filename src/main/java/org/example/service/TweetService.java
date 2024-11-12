package org.example.service;

import org.example.entity.Tweet;

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

}

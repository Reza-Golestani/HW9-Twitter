package org.example.service;

import org.example.entity.Tweet;
import org.example.repository.TagRepository;
import org.example.repository.Tweet_TagRepository;

import java.sql.SQLException;

public class TagService {

    public static void saveTags(Tweet tweet) throws SQLException {
        for (String tag : tweet.getTags()){
            long tagId;
            if(!TagRepository.isTagDuplicate(tag)){
                tagId = TagRepository.save(tag).getId();
            } else {
                tagId = TagRepository.getTagId(tag);
            }
            Tweet_TagRepository.save(tweet.getId(), tagId);
        }
    }

}

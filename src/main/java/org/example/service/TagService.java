package org.example.service;

import org.example.entity.Tweet;
import org.example.repository.TagRepository;
import org.example.repository.Tweet_TagRepository;

import java.sql.SQLException;

public class TagService {

    public static void saveTags(Tweet tweet) throws SQLException {
        for (String tag : tweet.getTags()){
            if(!TagRepository.isTagDuplicate(tag)){
//                TagRepository.save(tag);
                long tagId = TagRepository.save(tag).getId();
                Tweet_TagRepository.save(tweet.getId(), tagId);
            }
        }
    }

}

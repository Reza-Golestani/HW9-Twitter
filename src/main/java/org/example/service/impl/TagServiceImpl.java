package org.example.service.impl;

import org.example.entity.Tweet;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.repository.impl.Tweet_TagRepositoryImpl;

import java.sql.SQLException;

public class TagServiceImpl {

    public static void saveTags(Tweet tweet) throws SQLException {
        for (String tag : tweet.getTags()){
            long tagId;
            if(!TagRepositoryImpl.isTagDuplicate(tag)){
                tagId = TagRepositoryImpl.save(tag).getId();
            } else {
                tagId = TagRepositoryImpl.getTagId(tag);
            }
            Tweet_TagRepositoryImpl.save(tweet.getId(), tagId);
        }
    }

}

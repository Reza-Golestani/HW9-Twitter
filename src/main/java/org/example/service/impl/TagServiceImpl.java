package org.example.service.impl;

import org.example.entity.Tweet;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.repository.impl.Tweet_TagRepositoryImpl;
import org.example.service.TagService;

import java.sql.SQLException;

public class TagServiceImpl implements TagService {

    private TagRepositoryImpl tagRepositoryImpl = new TagRepositoryImpl();
    private Tweet_TagRepositoryImpl tweet_tagRepositoryImpl = new Tweet_TagRepositoryImpl();

    public void saveTags(Tweet tweet) throws SQLException {
        for (String tag : tweet.getTags()) {
            long tagId;
            if (!tagRepositoryImpl.isTagDuplicate(tag)) {
                tagId = tagRepositoryImpl.save(tag).getId();
            } else {
                tagId = tagRepositoryImpl.getTagId(tag);
            }
            tweet_tagRepositoryImpl.save(tweet.getId(), tagId);
        }
    }

}

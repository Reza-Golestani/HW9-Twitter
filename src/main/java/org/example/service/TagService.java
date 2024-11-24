package org.example.service;

import org.example.entity.Tweet;

import java.sql.SQLException;

public interface TagService {

    void saveTags(Tweet tweet) throws SQLException;

}

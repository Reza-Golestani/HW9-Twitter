package org.example.repository;

import org.example.entity.Tag;

import java.sql.SQLException;

public interface TagRepository {

    void initTable() throws SQLException;

    void delete(String tagName) throws SQLException;

    Tag save(String newTagTitle) throws SQLException;

    boolean isTagDuplicate(String name) throws SQLException;

    long getTagId(String name) throws SQLException;

}

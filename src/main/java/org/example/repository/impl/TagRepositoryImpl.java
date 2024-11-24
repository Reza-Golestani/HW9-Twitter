package org.example.repository.impl;

import org.example.Datasource;
import org.example.entity.Tag;
import org.example.repository.TagRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRepositoryImpl implements TagRepository {

    private final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tags (
            id bigserial PRIMARY KEY NOT NULL,
            name varchar(50) UNIQUE NOT NULL
            );
            """;

    public void initTable() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CREATE_TABLE);
        statement.execute();
        statement.close();
    }

    private final String INSERT_TAG = """
                INSERT INTO tags (name)
                VALUES (?)
                RETURNING id
                """;

    public Tag save(String newTagTitle) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(INSERT_TAG);
        statement.setString(1, newTagTitle);
        Tag newTag = new Tag();
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                newTag.setId(resultSet.getLong("id"));
                newTag.setName(newTagTitle);
            }
            statement.close();
            return newTag;
        }
    }

    private final String FIND_TAG = """
            SELECT id FROM tags
            WHERE name = ?
            """;

    public boolean isTagDuplicate(String name) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(FIND_TAG);
        statement.setString(1, name);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return true;
            }
            statement.close();
            return false;
        }
    }

    public long getTagId(String name) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(FIND_TAG);
        statement.setString(1, name);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        statement.close();
        return 0;
    }

    private final String DELETE_BY_NAME = """
            DELETE FROM tags
            WHERE name = ?
            """;

    public void delete(String tagName) throws SQLException {
        PreparedStatement statement = Datasource.getConnection().prepareStatement(DELETE_BY_NAME);
        statement.setString(1,tagName);
        statement.execute();
        statement.close();
    }
}

package org.example.repository;

import org.example.Datasource;
import org.example.entity.Tag;
import org.example.entity.Tweet;
import org.example.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TagRepository {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS tags (
            id bigserial PRIMARY KEY NOT NULL,
            name varchar(50) UNIQUE NOT NULL
            );
            """;

    public static void initTable() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CREATE_TABLE);
        statement.execute();
        statement.close();
    }

    private static String INSERT_TAG = """
                INSERT INTO tags (name)
                VALUES (?)
                RETURNING id
                """;

    public static Tag save(String newTagTitle) throws SQLException {
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

    private static String FIND_TAG = """
            SELECT id FROM tags
            WHERE name = ?
            """;

    public static boolean isTagDuplicate(String name) throws SQLException {
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
}

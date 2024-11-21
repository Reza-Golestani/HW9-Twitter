package org.example.repository;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.BCodec;
import org.example.Datasource;
import org.example.entity.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    Base64 base64 = new Base64();

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
            id bigserial PRIMARY KEY NOT NULL,
            email varchar(50) UNIQUE NOT NULL,
            username varchar(50) UNIQUE NOT NULL,
            password varchar(50) NOT NULL,
            displayed_name varchar(50) NOT NULL,
            bio varchar(250),
            created_at Date NOT NULL
            );
            """;

    public static void initTable() throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CREATE_TABLE);
        statement.execute();
        statement.close();
    }

    private static String INSERT_SQL = """
            INSERT INTO users (email, username, password, displayed_name, bio, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;

    public User saveUser(User user) throws SQLException {

        var statement = Datasource.getConnection().prepareStatement(INSERT_SQL);
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getUsername());
        statement.setString(3, new String(base64.encode(user.getPassword().getBytes())));
        statement.setString(4, user.getDisplayedName());
        statement.setString(5, user.getBio());
        statement.setDate(6, Date.valueOf(user.getCreated()));

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }
            statement.close();
            return user;
        }
    }

    private static String CHECK_EMAIL_AVAILABILITY_SQL = """
            SELECT id FROM users
            WHERE email = ?
            """;

private static String CHECK_USERNAME_AVAILABILITY_SQL = """
            SELECT id FROM users
            WHERE username = ?
            """;

    public boolean isEmailAvailable(String email) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CHECK_EMAIL_AVAILABILITY_SQL);
        statement.setString(1, email);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                statement.close();
                return false;
            }
        }
        statement.close();
        return true;
    }

    public boolean isUsernameAvailable(String username) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(CHECK_USERNAME_AVAILABILITY_SQL);
        statement.setString(1, username);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                statement.close();
                return false;
            }
        }
        statement.close();
        return true;
    }

    private static String FIND_BY_USERNAME = """
            SELECT * FROM users
            WHERE username = ?
            """;

    private static String FIND_BY_EMAIL = """
            SELECT * FROM users
            WHERE email = ?
            """;

    public User findByEmail(String email) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(FIND_BY_EMAIL);
        statement.setString(1, email);
        try (ResultSet resultSet = statement.executeQuery()) {
                User user = new User();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
                user.setEmail(resultSet.getString(2));
                user.setUsername(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                user.setDisplayedName(resultSet.getString(5));
                user.setBio(resultSet.getString(6));
                user.setCreated(resultSet.getDate(7).toLocalDate());
                statement.close();
                return user;
            }
            statement.close();
            return null;
        }
    }

    public User findByUsername(String username) throws SQLException {
        var statement = Datasource.getConnection().prepareStatement(FIND_BY_USERNAME);
        statement.setString(1, username);
        try (ResultSet resultSet = statement.executeQuery()) {
            User user = new User();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
                user.setEmail(resultSet.getString(2));
                user.setUsername(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                user.setDisplayedName(resultSet.getString(5));
                user.setBio(resultSet.getString(6));
                user.setCreated(resultSet.getDate(7).toLocalDate());
            statement.close();
            return user;
            }
            statement.close();
            return null;
        }
    }

    public void updateUser(User user) throws SQLException {
        long id = user.getId();
        String UPDATE_EMAIL = """
                UPDATE users
                SET email = ?
                WHERE id = ?
                """;
        String UPDATE_USERNAME = """
                UPDATE users
                SET username = ?
                WHERE id = ?
                """;
        String UPDATE_PASSWORD = """
                UPDATE users
                SET password = ?
                WHERE id = ?
                """;
        String UPDATE_DISPLAYED_NAME = """
                UPDATE users
                SET  displayed_name= ?
                WHERE id = ?
                """;
        String UPDATE_BIO = """
                UPDATE users
                SET bio = ?
                WHERE id = ?
                """;
        String UPDATE_USER = """
                UPDATE users
                SET email = ?,
                username = ?,
                password = ?,
                displayed_name= ?,
                bio = ?
                WHERE id = ?
                """;
        var statement = Datasource.getConnection().prepareStatement(UPDATE_USER);
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getUsername());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getDisplayedName());
        statement.setString(5, user.getBio());
        statement.setLong(6, user.getId());

        statement.executeUpdate();
        statement.close();
    }




}

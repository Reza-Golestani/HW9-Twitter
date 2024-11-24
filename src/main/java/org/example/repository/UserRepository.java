package org.example.repository;

import org.example.entity.User;

import java.sql.SQLException;

public interface UserRepository {

    void initTable() throws SQLException;

    void saveUser(User user) throws SQLException;

    User findByEmail(String email) throws SQLException;

    User findByUsername(String username) throws SQLException;

    void updateUser(User user) throws SQLException;

    boolean isEmailAvailable(String email) throws SQLException;

    boolean isUsernameAvailable(String username) throws SQLException;
}

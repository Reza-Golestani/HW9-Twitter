package org.example.service;

import org.example.entity.User;

import java.sql.SQLException;

public interface UserService {

    boolean isEmailAvailable(String email) throws SQLException;

    boolean isUsernameAvailable(String username) throws SQLException;

    void signUp(User user) throws SQLException;

    boolean signIn(String emailOrUsername, String password) throws SQLException;

    void signOut();

    void updateUser(User user) throws SQLException;

}

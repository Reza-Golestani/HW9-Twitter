package org.example.service;

import org.example.entity.User;

import java.sql.SQLException;

public interface UserService {

    void isEmailAvailable(String email) throws SQLException;

    void isUsernameAvailable(String username) throws SQLException;

    void signUp(User user) throws SQLException;

    void signIn(String emailOrUsername, String password) throws SQLException;

    void signOut();

    void updateUser(User user) throws SQLException;

    void oldPasswordCheck(String oldPassword);

}

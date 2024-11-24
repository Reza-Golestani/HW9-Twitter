package org.example.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.example.entity.User;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.service.UserService;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private UserRepositoryImpl userRepository = new UserRepositoryImpl();

    public static User loggedInUser;

    private Base64 base64 = new Base64();

    public boolean isEmailAvailable(String email) throws SQLException {
        return userRepository.isEmailAvailable(email);
    }

    public boolean isUsernameAvailable(String username) throws SQLException {
        return userRepository.isUsernameAvailable(username);
    }

    public void signUp(User user) throws SQLException {
        userRepository.saveUser(user);
    }

    public boolean signIn(String emailOrUsername, String password) throws SQLException {
        if (userRepository.findByUsername(emailOrUsername) != null) {
            if (userRepository.findByUsername(emailOrUsername).getPassword().equals(new String(base64.encode(password.getBytes())))) {
                loggedInUser = userRepository.findByUsername(emailOrUsername);
                return true;
            }
        }
        if (userRepository.findByEmail(emailOrUsername) != null) {
            if (userRepository.findByEmail(emailOrUsername).getPassword().equals(new String(base64.encode(password.getBytes())))) {
                loggedInUser = userRepository.findByEmail(emailOrUsername);
                return true;
            }
        }
        return false;
    }

    public void signOut() {
        loggedInUser = null;
    }

    public void updateUser(User user) throws SQLException {
        userRepository.updateUser(user);
    }
}



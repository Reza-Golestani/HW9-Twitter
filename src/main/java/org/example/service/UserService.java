package org.example.service;

import org.apache.commons.codec.binary.Base64;
import org.example.entity.User;
import org.example.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class UserService {

    static UserRepository userRepository = new UserRepository();

    public static User loggedInUser;

    static Base64 base64 = new Base64();

    public static boolean isEmailAvailable(String email) throws SQLException {
        return userRepository.isEmailAvailable(email);
    }

    public static boolean isUsernameAvailable(String username) throws SQLException {
        return userRepository.isUsernameAvailable(username);
    }

    public static void signUp(User user) throws SQLException {
        userRepository.saveUser(user);
    }

    public static boolean signIn(String emailOrUsername, String password) throws SQLException {
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

    public static void signOut() {
        loggedInUser = null;
    }

    public static void updateUser(User user) throws SQLException {
        userRepository.updateUser(user);
    }
}



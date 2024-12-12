package org.example.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.example.entity.User;
import org.example.exception.DuplicateEmailUsernameException;
import org.example.exception.WrongUsernameOrPasswordException;
import org.example.repository.impl.UserRepositoryImpl;
import org.example.service.UserService;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {

    private final UserRepositoryImpl userRepository = new UserRepositoryImpl();

    public static User loggedInUser;

    private final Base64 base64 = new Base64();

    @Override
    public void isEmailAvailable(String email) throws SQLException {
        if (!userRepository.isEmailAvailable(email)) {
            throw new DuplicateEmailUsernameException("This email is already in use. Try another one: ");
        }
    }

    @Override
    public void isUsernameAvailable(String username) throws SQLException {
        if (!userRepository.isEmailAvailable(username)) {
            throw new DuplicateEmailUsernameException("This username is already in use. Try another one: ");
        }
    }

    @Override
    public void signUp(User user) throws SQLException {
        userRepository.saveUser(user);
    }

    @Override
    public void signIn(String emailOrUsername, String password) throws SQLException {
        if (userRepository.findByUsername(emailOrUsername) != null) {
            if (userRepository.findByUsername(emailOrUsername).getPassword().equals(new String(base64.encode(password.getBytes())))) {
                loggedInUser = userRepository.findByUsername(emailOrUsername);
                return;
            }
        }
        if (userRepository.findByEmail(emailOrUsername) != null) {
            if (userRepository.findByEmail(emailOrUsername).getPassword().equals(new String(base64.encode(password.getBytes())))) {
                loggedInUser = userRepository.findByEmail(emailOrUsername);
                return;
            }
        }
        throw new WrongUsernameOrPasswordException("\n>>> Invalid email/username or password!");
    }

    @Override
    public void signOut() {
        loggedInUser = null;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        userRepository.updateUser(user);
    }

    @Override
    public void oldPasswordCheck(String oldPassword){
       if (!UserServiceImpl.loggedInUser.getPassword().equals(new String(base64.encode(oldPassword.getBytes())))){
           throw new WrongUsernameOrPasswordException("\n>>> Wrong password!");
       }
    }
}



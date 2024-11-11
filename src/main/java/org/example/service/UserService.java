package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class UserService {

    static UserRepository userRepository = new UserRepository();

    public static User loggedInUser;


    public static void signUp() throws SQLException {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n---------------- Sign Up ----------------\n");
        User user = new User();

        System.out.print("Please enter your email: ");
        String email = sc.nextLine();
        while (!userRepository.isEmailAvailable(email)) {
            System.out.print("This email is already in use. Please try another one: ");
            email = sc.nextLine();
        }
        user.setEmail(email);

        System.out.print("Please enter your username: ");
        String username = sc.nextLine();
        while (!userRepository.isUsernameAvailable(username)) {
            System.out.print("This username is already in use. Please try another one: ");
            username = sc.nextLine();
        }
        user.setUsername(username);

        System.out.print("Please enter your password: ");
        String password = sc.nextLine();
        user.setPassword(password);

        System.out.print("please enter your displayed name: ");
        String displayName = sc.nextLine();
        user.setDisplayName(displayName);

        System.out.println("please enter your bio: ");
        String bio = sc.nextLine();
        user.setBio(bio);

        user.setCreated(LocalDate.now());

//        loggedInUser = userRepository.saveUser(user);

        userRepository.saveUser(user);

        System.out.println("\n>>> User successfully signed up!");
    }

    public static void signIn() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n-------------- Sign In ----------------\n");
        System.out.print("Please enter your email/username: ");
        String emailOrUsername = sc.nextLine();
        System.out.print("Please enter your password: ");
        String password = sc.nextLine();
        if (userRepository.findByUsername(emailOrUsername) != null) {
            if (userRepository.findByUsername(emailOrUsername).getPassword().equals(password)) {
                loggedInUser = userRepository.findByUsername(emailOrUsername);
                System.out.println("\n>>> Logged in successfully!");
            }
        }
        if (userRepository.findByEmail(emailOrUsername) != null) {
            if (userRepository.findByEmail(emailOrUsername).getPassword().equals(password)) {
                loggedInUser = userRepository.findByEmail(emailOrUsername);
                System.out.println("\n>>> Logged in successfully!");
            }
        }

        if (loggedInUser == null) {
            System.out.println("\n>>> Invalid email/username or password!");
        }

    }

}



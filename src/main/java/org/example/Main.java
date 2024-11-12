package org.example;

import org.example.entity.Tweet;
import org.example.repository.*;
import org.example.service.TweetService;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws SQLException {

        UserRepository.initTable();
        TweetRepository.initTable();
        TagRepository.initTable();
        ReactionRepository.initTable();
        Tweet_TagRepository.initTable();

        while (true) {
            while (UserService.loggedInUser == null) {
                welcomePage();
            }

            while (UserService.loggedInUser != null) {
                homePage();
            }
        }
    }

    private static void welcomePage() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--------- Welcome to Twitter --------\n");
        System.out.println("1- Sign in");
        System.out.println("2- Sign up\n");
        System.out.print(">>> Enter your choice: ");
        String choice = sc.nextLine();
        if (choice.equals("1")) {
            UserService.signIn();
        } else if (choice.equals("2")) {
            UserService.signUp();
        }
    }

    private static void homePage() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------------- Home Page ------------\n");
        System.out.println("1- All tweets"); // view, react, retweet
        System.out.println("2- Your tweets"); // view, edit, delete
        System.out.println("3- Post a new tweet"); // create
        System.out.println("4- Edit your profile");
        System.out.println("0- Sign out");
        System.out.print("\n>>> Enter your choice: ");
        String choice = sc.nextLine();
        if (choice.equals("1")) {

        } else if (choice.equals("2")) {

        } else if (choice.equals("3")) {
            newTweetMenu();
        } else if (choice.equals("4")) {
            editProfileMenu();
        } else if (choice.equals("0")) {
            UserService.signOut();
        }
    }

    private static void editProfileMenu() throws SQLException {

        // todo: show the current values to user while asking for new ones
        UserRepository userRepository = new UserRepository();
        Scanner sc = new Scanner(System.in);

        System.out.println("\n------------ Edit Profile -----------\n");
        System.out.println("1- Edit your Email");
        System.out.println("2- Edit your Username");
        System.out.println("3- Edit your Password");
        System.out.println("4- Edit your displayed name");
        System.out.println("5- Edit your bio");
        System.out.println("0- Back");
        System.out.print("\n>>> Enter your choice: ");
        String choice = sc.nextLine();
        if (choice.equals("1")) {
            System.out.print("Enter your new email: ");
            String newEmail = sc.nextLine();
            while (!userRepository.isEmailAvailable(newEmail)) {
                System.out.print("This email is already in use. Try another one: ");
                newEmail = sc.nextLine();
            }
            UserService.loggedInUser.setEmail(newEmail);
            UserService.updateUser(UserService.loggedInUser);
            System.out.println("\n>>> Email successfully updated!");
        } else if (choice.equals("2")) {
            System.out.print("Enter your new username: ");
            String newUsername = sc.nextLine();
            while (!userRepository.isUsernameAvailable(newUsername)) {
                System.out.print("This username is already in use. Try another one: ");
                newUsername = sc.nextLine();
            }
            UserService.loggedInUser.setUsername(newUsername);
            UserService.updateUser(UserService.loggedInUser);
        } else if (choice.equals("3")) {
            System.out.print("Enter your old password: ");
            String oldPassword = sc.nextLine();
            if (!UserService.loggedInUser.getPassword().equals(oldPassword)) {
                System.out.println(">>> Wrong password!");
                editProfileMenu();
            }
            System.out.print("Enter your new password: ");
            String newPassword = sc.nextLine();
            UserService.loggedInUser.setPassword(newPassword);
            UserService.updateUser(UserService.loggedInUser);
            System.out.println("\n>>> Password successfully updated!");
        } else if (choice.equals("4")) {
            System.out.print("Enter your new displayed name: ");
            String newDisplayedName = sc.nextLine();
            UserService.loggedInUser.setDisplayedName(newDisplayedName);
            UserService.updateUser(UserService.loggedInUser);
            System.out.println("\n>>> Displayed name successfully updated!");
        } else if (choice.equals("5")) {
            System.out.println("Enter your new bio: ");
            String newBio = sc.nextLine();
            UserService.loggedInUser.setBio(newBio);
            UserService.updateUser(UserService.loggedInUser);
            System.out.println("\n>>> Bio successfully updated!");
        } else if (choice.equals("0")) {
            homePage();
        }
    }

    public static void newTweetMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------------ New Tweet -------------\n");
        System.out.println("Enter your new tweet (Max: 280 characters): ");
        String newTweetText = sc.nextLine();
        while (newTweetText.length() > 280) {
            System.out.println("Too long tweet! Max allowed length is 280 characters, try again: ");
            newTweetText = sc.nextLine();
        }
        System.out.println("Enter tags (enter '0' to finish): ");
        Set<String> tagTitles = new HashSet<>();
        String newTagTitle = sc.nextLine();
        while (!newTagTitle.equals("0")) {
            newTagTitle = sc.nextLine();
            tagTitles.add(newTagTitle);
        }
        System.out.print("\nPost this new tweet? (y/n): ");
        String choice = sc.nextLine();
        if (choice.equals("y")) {
            Tweet newTweet = TweetService.create(newTweetText, tagTitles);
            TweetRepository.save(newTweet);
            System.out.println("\n>>> New tweet posted!");
        } else if (choice.equals("n")) {
            System.out.println("\n>>> New tweet canceled!");
            homePage();
        }
    }


}

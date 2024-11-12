package org.example;

import org.example.repository.UserRepository;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        UserRepository.initTable();

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
        System.out.println("1- All tweets");
        System.out.println("2- Your tweets");
        System.out.println("3- Post a new tweet");
        System.out.println("4- Edit your profile");
        System.out.println("0- Sign out");
        System.out.print("\n>>> Enter your choice: ");
        String choice = sc.nextLine();
        if (choice.equals("1")) {

        } else if (choice.equals("2")) {

        } else if (choice.equals("3")) {

        } else if (choice.equals("4")) {
            editProfileMenu();
        } else if (choice.equals("0")) {
            UserService.signOut();
        }
    }

    private static void editProfileMenu() throws SQLException {
        // todo: show the current values to user while asking for new ones
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
            UserService.loggedInUser.setEmail(newEmail);
            UserService.updateUser(UserService.loggedInUser);
        } else if (choice.equals("2")) {
            System.out.print("Enter your new username: ");
            String newUsername = sc.nextLine();
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
        } else if (choice.equals("4")) {
            System.out.print("Enter your new displayed name: ");
            String newDisplayedName = sc.nextLine();
            UserService.loggedInUser.setDisplayedName(newDisplayedName);
            UserService.updateUser(UserService.loggedInUser);
        } else if (choice.equals("5")) {
            System.out.println("Enter your new bio: ");
            String newBio = sc.nextLine();
            UserService.loggedInUser.setBio(newBio);
            UserService.updateUser(UserService.loggedInUser);
        } else if (choice.equals("0")) {
            homePage();
        }
    }
}

package org.example;

import org.example.repository.UserRepository;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        UserRepository.initTable();
        Scanner sc = new Scanner(System.in);

        while(true){
            while(UserService.loggedInUser == null){
                System.out.println("\n--------- Welcome to Twitter --------\n");
                System.out.println("1- Sign in");
                System.out.println("2- Sign up\n");
                System.out.print(">>> Enter your choice: ");
                int choice = sc.nextInt();
                if (choice == 1) {
                    UserService.signIn();
                } else if (choice == 2) {
                    UserService.signUp();
                }
            }

            while (UserService.loggedInUser != null){

            }
        }

    }
}

package org.example;

import org.example.entity.Tweet;
import org.example.repository.*;
import org.example.service.ReactionService;
import org.example.service.TagService;
import org.example.service.TweetService;
import org.example.service.UserService;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws SQLException {

        UserRepository.initTable();
        TagRepository.initTable();
        TweetRepository.initTable();
        Tweet_TagRepository.initTable();
        ReactionRepository.initTable();

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
            allTweetsMenu();
        } else if (choice.equals("2")) {
            yourTweetsMenu();
        } else if (choice.equals("3")) {
            newTweetMenu();
        } else if (choice.equals("4")) {
            editProfileMenu();
        } else if (choice.equals("0")) {
            UserService.signOut();
        }
    }

    private static void yourTweetsMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------------- Your Tweets ------------\n");
        int index = 0;
        for (Tweet tweet : TweetService.getAll(UserService.loggedInUser)) {
            System.out.println("\n--< " + ++index + " >--\n" + printTweet(tweet) + "\n");
        }
        System.out.print("\n>>> Enter a tweet's index to view or '0' to go back: ");
        String choice = sc.nextLine();
        if (choice.equals("0")) {
            homePage();
        } else if (Integer.parseInt(choice) <= TweetService.getAll(UserService.loggedInUser).size()) {
            tweetView(TweetService.getAll(UserService.loggedInUser).get(Integer.parseInt(choice) - 1));
        }
    }

    private static void allTweetsMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------------- All Tweets ------------\n");
        int index = 0;
        for (Tweet tweet : TweetService.getAll()) {
            System.out.println("\n<<< " + ++index + " >>>\n" + printTweet(tweet) + "\n");
        }
        System.out.print("\n>>> Enter a tweet's index to view or '0' to go back: ");
        String choice = sc.nextLine();
        if (choice.equals("0")) {
            homePage();
        } else if (Integer.parseInt(choice) <= TweetService.getAll().size()) {
            tweetView(TweetService.getAll().get(Integer.parseInt(choice) - 1));
        }
    }

    private static void tweetView(Tweet tweet) throws SQLException {
        System.out.println("\n------------- Tweet View -------------\n");
        System.out.println(printTweet(tweet));
        System.out.println("\n-> Note! <-\nYour current reaction to this tweet: " +
                ReactionService.currentReaction(UserService.loggedInUser.getId(), tweet.getId()));
        String extraOptions = "5- Edit\n6- Delete\n";
        System.out.println("""
                \n1- Like
                2- Dislike
                3- Clear your reaction
                4- Retweet""");
        if (UserService.loggedInUser.getId() == tweet.getWriter().getId())
            System.out.println(extraOptions);
        System.out.println("0- Back");
        System.out.print("\n>>> Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();
        if (choice.equals("0")) {
            homePage();
        } else if (choice.equals("1")) {
            ReactionService.like(UserService.loggedInUser.getId(), tweet.getId());
            System.out.println(">>> Now, your reaction to this post is: LIKE");
            tweetView(tweet);
        } else if (choice.equals("2")) {
            ReactionService.dislike(UserService.loggedInUser.getId(), tweet.getId());
            System.out.println(">>> Now, your reaction to this post is: DISLIKE");
            tweetView(tweet);
        } else if (choice.equals("3")) {
            ReactionService.clearReaction(UserService.loggedInUser.getId(), tweet.getId());
            System.out.println(">>> Now, you have no reaction to this post!");
            tweetView(tweet);
        } else if (choice.equals("4")) {
            retweet(UserService.loggedInUser.getId(), tweet);
        } else if (UserService.loggedInUser.getId() == tweet.getWriter().getId()) {
            if (choice.equals("5")) {
                edit(tweet);
            } else if (choice.equals("6")) {
                Delete(tweet);
            }
        }
    }

    private static void Delete(Tweet tweet) throws SQLException {
        System.out.println("\n------------ Delete Tweet ------------\n");
        System.out.println("Note! --> You are about to delete this tweet:\n");
        System.out.println(printTweet(tweet));
        System.out.print("\n>>> Are you sure? (y/n): ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();
        if (choice.equals("y")) {
            TweetService.deleteTweet(tweet);
            System.out.println("\n>>> Deleted successfully!");
        } else if (choice.equals("n")) {
            System.out.println("\n>>> Deleting canceled!");
            tweetView(tweet);
        }
    }

    private static void edit(Tweet tweet) throws SQLException {
        System.out.println("\n------------ Edit Tweet ------------\n");
        System.out.println("Note! --> You are about to edit this tweet:\n");
        System.out.println(printTweet(tweet));
        System.out.print("""
                What dou you want to edit?
                               \s
                1- Tweet text
                2- Tweet tags
                0- Back
                               \s
                >>> Enter your choice:""");
        Scanner sc = new Scanner(System.in);
        String newText = tweet.getText();
        Set<String> newTagTitles = tweet.getTags();
        String choice = sc.nextLine();
        if (choice.equals("0")) {
            tweetView(tweet);
        } else if (choice.equals("1")) {
            System.out.println("\nEnter your new text (Max: 280 characters): ");
            newText = sc.nextLine();
            while (newText.length() > 280) {
                System.out.println("Too long tweet! Max allowed length is 280 characters, try again: ");
                newText = sc.nextLine();
            }
        } else if (choice.equals("2")) {
            System.out.println("\nEnter new tags (enter '0' to finish): ");
            newTagTitles = new HashSet<>();
            String newTagTitle = sc.nextLine();
            while (!newTagTitle.equals("0")) {
                newTagTitles.add(newTagTitle);
                newTagTitle = sc.nextLine();
            }
        }
        System.out.print("\nApply changes? (y/n): ");
        String choice2 = sc.nextLine();
        if (choice2.equals("y")) {
            TweetService.editText(tweet, newText);
            TweetService.editTags(tweet, newTagTitles);
            System.out.println("\n>>> Tweet edited successfully!");
        } else if (choice2.equals("n")) {
            System.out.println("\n>>> Editing canceled!");
            tweetView(tweet);
        }

    }

    private static void retweet(long userId, Tweet tweet) throws SQLException {
        System.out.println("\n------------ Retweet -------------\n");
        System.out.println("Note! --> You are about to retweet this tweet:\n");
        System.out.println(printTweet(tweet));
        postTweet("retweet", tweet);
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
        System.out.println("\n------------ New Tweet -------------\n");
        postTweet("new", null);
    }

    private static void postTweet(String mode, Tweet retweeted) throws SQLException {
        Scanner sc = new Scanner(System.in);
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
            tagTitles.add(newTagTitle);
            newTagTitle = sc.nextLine();
        }
        System.out.print("\nPost this new tweet? (y/n): ");
        String choice = sc.nextLine();
        if (choice.equals("y")) {
            Tweet newTweet = TweetService.create(newTweetText, tagTitles);
            newTweet.setId(TweetRepository.save(newTweet).getId());
            TagService.saveTags(newTweet);
            if (mode.equals("new")) {
                System.out.println("\n>>> New tweet posted!");
                homePage();
            } else if (mode.equals("retweet") && retweeted != null) {
                TweetService.setRetweeted(newTweet.getId(), retweeted.getId());
                System.out.println("\n>>> Retweet done and new tweet posted!");
                tweetView(retweeted);
            }

        } else if (choice.equals("n")) {
            if (mode.equals("new")) {
                System.out.println("\n>>> New tweet canceled!");
                homePage();
            } else if (mode.equals("retweet") && retweeted != null) {
                System.out.println("\n>>> Retweet canceled!");
                tweetView(retweeted);
            }
        }
    }

    public static String printTweet(Tweet tweet) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String stringForm =
                tweet.getWriter().getDisplayedName() + ":\n" + tweet.getText() + "\n----------------------------------------\n";

        if (!tweet.getTags().isEmpty()) {
            stringForm += tweet.getTags() + "\n";
        }

        stringForm += "Posted at " + tweet.getCreatedAt().format(formatter);

        if (tweet.getEditedAt() != null)
            stringForm += " (edited)";

        stringForm += "\n" + "Likes: " + ReactionService.reactionsCount(tweet.getId(),
                "like") + ",    Dislikes: " + ReactionService.reactionsCount(tweet.getId(),
                "dislike") + ",    Retweets: " + ReactionService.reactionsCount(tweet.getId(),
                "retweet");

        return stringForm;
    }
}




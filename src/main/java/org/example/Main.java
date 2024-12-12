package org.example;

import org.apache.commons.codec.EncoderException;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.exception.AllowableTweetLengthException;
import org.example.exception.DuplicateEmailUsernameException;
import org.example.exception.WrongUsernameOrPasswordException;
import org.example.repository.impl.*;
import org.example.service.impl.ReactionServiceImpl;
import org.example.service.impl.TagServiceImpl;
import org.example.service.impl.TweetServiceImpl;
import org.example.service.impl.UserServiceImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final UserServiceImpl userServiceImpl = new UserServiceImpl();
    private static final ReactionServiceImpl reactionServiceImpl = new ReactionServiceImpl();
    private static final TagServiceImpl tagServiceImpl = new TagServiceImpl();
    private static final TweetServiceImpl tweetServiceImpl = new TweetServiceImpl();

    public static void main(String[] args) throws SQLException, EncoderException {

        initTables();

        while (true) {
            while (UserServiceImpl.loggedInUser == null) {
                welcomePage();
            }

            while (UserServiceImpl.loggedInUser != null) {
                homePage();
            }
        }
    }

    public static void initTables() throws SQLException {
        UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();
        TagRepositoryImpl tagRepositoryImpl = new TagRepositoryImpl();
        TweetRepositoryImpl tweetRepositoryImpl = new TweetRepositoryImpl();
        Tweet_TagRepositoryImpl tweet_tagRepositoryImpl = new Tweet_TagRepositoryImpl();
        ReactionRepositoryImpl reactionRepositoryImpl = new ReactionRepositoryImpl();

        userRepositoryImpl.initTable();
        tagRepositoryImpl.initTable();
        tweetRepositoryImpl.initTable();
        tweet_tagRepositoryImpl.initTable();
        reactionRepositoryImpl.initTable();
    }

    private static void welcomePage() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--------- Welcome to Twitter --------\n");
        System.out.println("1- Sign in");
        System.out.println("2- Sign up\n");
        System.out.print(">>> Enter your choice: ");
        String choice = sc.nextLine();
        if (choice.equals("1")) {
            signIn();
        } else if (choice.equals("2")) {
            signUp();
        }
    }

    public static void signUp() throws SQLException {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n---------------- Sign Up ----------------\n");
        User user = new User();

        System.out.print("Please enter your email: ");
        String email = sc.nextLine();

        while (true) {
            try {
                userServiceImpl.isEmailAvailable(email);
                break;
            } catch (DuplicateEmailUsernameException e) {
                System.out.println(e.getMessage());
                email = sc.nextLine();
            }
        }
        user.setEmail(email);

        System.out.print("Please enter your username: ");
        String username = sc.nextLine();
        while (true) {
            try {
                userServiceImpl.isUsernameAvailable(username);
                break;
            } catch (DuplicateEmailUsernameException e) {
                System.out.println(e.getMessage());
                username = sc.nextLine();
            }
        }
        user.setUsername(username);

        System.out.print("Please enter your password: ");
        String password = sc.nextLine();
        user.setPassword(password);

        System.out.print("please enter your displayed name: ");
        String displayedName = sc.nextLine();
        user.setDisplayedName(displayedName);

        System.out.println("please enter your bio: ");
        String bio = sc.nextLine();
        user.setBio(bio);

        user.setCreated(LocalDate.now());

        userServiceImpl.signUp(user);

        System.out.println("\n>>> User successfully signed up!");
    }

    public static void signIn() throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n-------------- Sign In ----------------\n");
        System.out.print("Please enter your email/username: ");
        String emailOrUsername = sc.nextLine();
        System.out.print("Please enter your password: ");
        String password = sc.nextLine();
        try {
            userServiceImpl.signIn(emailOrUsername, password);
            System.out.println("\n>>> Logged in successfully!");
        } catch (WrongUsernameOrPasswordException e) {
            System.out.println(e.getMessage());
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
            userServiceImpl.signOut();
            System.out.println("\nYou have been logged out successfully!");
        }
    }

    private static void yourTweetsMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------------- Your Tweets ------------\n");
        int index = 0;
        for (Tweet tweet : tweetServiceImpl.getAll(UserServiceImpl.loggedInUser)) {
            System.out.println("\n--< " + ++index + " >--\n" + printTweet(tweet, 0) + "\n");
        }
        System.out.print("\n>>> Enter a tweet's index to view or '0' to go back: ");
        String choice = sc.nextLine();
        if (choice.equals("0")) {
            homePage();
        } else if (Integer.parseInt(choice) <= tweetServiceImpl.getAll(UserServiceImpl.loggedInUser).size()) {
            tweetView(tweetServiceImpl.getAll(UserServiceImpl.loggedInUser).get(Integer.parseInt(choice) - 1));
        }
    }

    private static void allTweetsMenu() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n------------- All Tweets ------------\n");
        int index = 0;
        for (Tweet tweet : tweetServiceImpl.getAll()) {
            System.out.println("\n<<< " + ++index + " >>>\n" + printTweet(tweet, 0) + "\n");
        }
        System.out.print("\n>>> Enter a tweet's index to view or '0' to go back: ");
        String choice = sc.nextLine();
        if (choice.equals("0")) {
            homePage();
        } else if (Integer.parseInt(choice) <= tweetServiceImpl.getAll().size()) {
            tweetView(tweetServiceImpl.getAll().get(Integer.parseInt(choice) - 1));
        }
    }

    private static void tweetView(Tweet tweet) throws SQLException {
        System.out.println("\n------------- Tweet View -------------\n");
        System.out.println(printTweet(tweet, 0));
        System.out.println("\n-> Note! <-\nYour current reaction to this tweet: " +
                reactionServiceImpl.currentReaction(UserServiceImpl.loggedInUser.getId(),
                        tweet.getId()));
        String extraOptions = "5- Edit\n6- Delete\n";
        System.out.println("""
                \n1- Like
                2- Dislike
                3- Clear your reaction
                4- Retweet""");
        if (UserServiceImpl.loggedInUser.getId() == tweet.getWriter().getId())
            System.out.println(extraOptions);
        System.out.println("0- Back");
        System.out.print("\n>>> Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();
        if (choice.equals("0")) {
            homePage();
        } else if (choice.equals("1")) {
            reactionServiceImpl.like(UserServiceImpl.loggedInUser.getId(), tweet.getId());
            System.out.println(">>> Now, your reaction to this post is: LIKE");
            tweetView(tweet);
        } else if (choice.equals("2")) {
            reactionServiceImpl.dislike(UserServiceImpl.loggedInUser.getId(), tweet.getId());
            System.out.println(">>> Now, your reaction to this post is: DISLIKE");
            tweetView(tweet);
        } else if (choice.equals("3")) {
            reactionServiceImpl.clearReaction(UserServiceImpl.loggedInUser.getId(), tweet.getId());
            System.out.println(">>> Now, you have no reaction to this post!");
            tweetView(tweet);
        } else if (choice.equals("4")) {
            retweet(UserServiceImpl.loggedInUser.getId(), tweet);
        } else if (UserServiceImpl.loggedInUser.getId() == tweet.getWriter().getId()) {
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
        System.out.println(printTweet(tweet, 0));
        System.out.print("\n>>> Are you sure? (y/n): ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();
        if (choice.equals("y")) {
            tweetServiceImpl.deleteTweet(tweet);
            System.out.println("\n>>> Deleted successfully!");
            yourTweetsMenu();
        } else if (choice.equals("n")) {
            System.out.println("\n>>> Deleting canceled!");
            tweetView(tweet);
        }
    }

    private static void edit(Tweet tweet) throws SQLException {
        System.out.println("\n------------ Edit Tweet ------------\n");
        System.out.println("Note! --> You are about to edit this tweet:\n");
        System.out.println(printTweet(tweet, 0));
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
            while (true) {
                try {
                    tweetServiceImpl.tweetLengthCheck(newText.length());
                    break;
                } catch (AllowableTweetLengthException e) {
                    System.out.println(e.getMessage());
                    newText = sc.nextLine();
                }
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
            tweetServiceImpl.editText(tweet, newText);
            tweetServiceImpl.editTags(tweet, newTagTitles);
            System.out.println("\n>>> Tweet edited successfully!");
        } else if (choice2.equals("n")) {
            System.out.println("\n>>> Editing canceled!");
            tweetView(tweet);
        }

    }

    private static void retweet(long userId, Tweet tweet) throws SQLException {
        System.out.println("\n------------ Retweet -------------\n");
        System.out.println("Note! --> You are about to retweet this tweet:\n");
        System.out.println(printTweet(tweet, 0));
        postTweet("retweet", tweet);
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
            while (true) {
                try {
                    userServiceImpl.isEmailAvailable(newEmail);
                    break;
                } catch (DuplicateEmailUsernameException e) {
                    System.out.println(e.getMessage());
                    newEmail = sc.nextLine();
                }
            }
            UserServiceImpl.loggedInUser.setEmail(newEmail);
            userServiceImpl.updateUser(UserServiceImpl.loggedInUser);
            System.out.println("\n>>> Email successfully updated!");
        } else if (choice.equals("2")) {
            System.out.print("Enter your new username: ");
            String newUsername = sc.nextLine();
            while (true) {
                try {
                    userServiceImpl.isUsernameAvailable(newUsername);
                    break;
                } catch (DuplicateEmailUsernameException e) {
                    System.out.println(e.getMessage());
                    newUsername = sc.nextLine();
                }
            }
            UserServiceImpl.loggedInUser.setUsername(newUsername);
            userServiceImpl.updateUser(UserServiceImpl.loggedInUser);
        } else if (choice.equals("3")) {
            System.out.print("Enter your old password: ");
            String oldPassword = sc.nextLine();
            try {
                userServiceImpl.oldPasswordCheck(oldPassword);
            } catch (WrongUsernameOrPasswordException e) {
                System.out.println(e.getMessage());
                editProfileMenu();
            }

            System.out.print("Enter your new password: ");
            String newPassword = sc.nextLine();
            UserServiceImpl.loggedInUser.setPassword(newPassword);
            userServiceImpl.updateUser(UserServiceImpl.loggedInUser);
            System.out.println("\n>>> Password successfully updated!");
        } else if (choice.equals("4")) {
            System.out.print("Enter your new displayed name: ");
            String newDisplayedName = sc.nextLine();
            UserServiceImpl.loggedInUser.setDisplayedName(newDisplayedName);
            userServiceImpl.updateUser(UserServiceImpl.loggedInUser);
            System.out.println("\n>>> Displayed name successfully updated!");
        } else if (choice.equals("5")) {
            System.out.println("Enter your new bio: ");
            String newBio = sc.nextLine();
            UserServiceImpl.loggedInUser.setBio(newBio);
            userServiceImpl.updateUser(UserServiceImpl.loggedInUser);
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
        System.out.println("\nEnter your new tweet (Max: 280 characters): ");
        String newTweetText = sc.nextLine();
        while (true) {
            try {
                tweetServiceImpl.tweetLengthCheck(newTweetText.length());
                break;
            } catch (AllowableTweetLengthException e) {
                System.out.println(e.getMessage());
                newTweetText = sc.nextLine();
            }
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
            Tweet newTweet = tweetServiceImpl.create(newTweetText, tagTitles);
            newTweet.setId(tweetServiceImpl.save(newTweet));
            tagServiceImpl.saveTags(newTweet);
            if (mode.equals("new")) {
                System.out.println("\n>>> New tweet posted!");
                homePage();
            } else if (mode.equals("retweet") && retweeted != null) {
                tweetServiceImpl.setRetweeted(newTweet.getId(), retweeted.getId());
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

    public static String printTweet(Tweet tweet, int tabs) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int counter = tabs;

        String stringForm =
                printTabs(tabs) + tweet.getWriter().getDisplayedName() + ":\n" + printTabs(tabs) + tweet.getText() +
                        "\n" + printTabs(tabs) +
                        "----------------------------------------\n";

        if (!tweet.getTags().isEmpty()) {
            stringForm += printTabs(tabs) + tweet.getTags() + "\n";
        }

        stringForm += printTabs(tabs) + "Posted at " + tweet.getCreatedAt().format(formatter);

        if (tweet.getEditedAt() != null)
            stringForm += " (edited)";

        stringForm += "\n" + printTabs(tabs) + "Likes: " + reactionServiceImpl.reactionsCount(tweet.getId(),
                "like") + ",    Dislikes: " + reactionServiceImpl.reactionsCount(tweet.getId(),
                "dislike") + ",    Retweets: " + reactionServiceImpl.reactionsCount(tweet.getId(),
                "retweet");

        if (tweet.getRetweeted() != null) {
            counter++;
            stringForm += "\n" + printTabs(tabs) + "------> Retweeted:\n" + printTweet(tweet.getRetweeted(), counter);
        }

        return stringForm;
    }

    public static String printTabs(int count) {
        String tabs = "";
        for (int i = 0; i < 2 * count; i++) {
            tabs += "\t";
        }
        return tabs;
    }
}




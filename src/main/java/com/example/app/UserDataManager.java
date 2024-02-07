package com.example.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.example.app.User;

public class UserDataManager {

    private static final String USERS_FILE = "users.ser";

    public static void saveUserList(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> loadUserList() {
        File userFile = new File(USERS_FILE);
        if (userFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
                return (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(); //Empty list if file doesn't exist or there's an error
    }

    public static void initializeUserList() {
        File userFile = new File(USERS_FILE);
        if (!userFile.exists()) {
            List<User> users = new ArrayList<>();
            users.add(new User("admin", "admin")); //Add admin user
            saveUserList(users);
        }
    }

    public static void printUsers() {
        List<User> users = loadUserList();
        if (users.isEmpty()) {
            System.out.println("No users have been registered yet.");
        } else {
            System.out.println("Registered Users:");
            for (User user : users) {
                System.out.printf("Username: %s, Password: %s, ID Number: %s, Name: %s %s, Email: %s\n",
                        user.getUsername(), user.getPassword(), user.getIdNumber(), user.getName(), user.getSurname(), user.getEmail());
            }
        }
    }

    //clear the list
    public static void clearUserList() {
        List<User> users = new ArrayList<>(); //Create an empty list
        saveUserList(users); //Save the empty list back to the file
    }
}

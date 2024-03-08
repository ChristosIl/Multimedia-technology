package com.example.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDataManager {

    private static final String USERS_FILE = "medialab"+ File.separator +"users.ser";
    private static UserDataManager instance;
    private List<User> users;

    private UserDataManager() {
        this.users = loadUserList();
    }

    public static synchronized UserDataManager getInstance() {
        if (instance == null) {
            instance = new UserDataManager();
        }
        return instance;
    }

    public void saveUserList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public List<User> loadUserList() {
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

    public void initializeUserList() {
        File userFile = new File(USERS_FILE);
        if (!userFile.exists()) {
            // Directly modify the instance's users list
            this.users.clear(); // Ensure it's empty before initialization
            this.users.add(new User("medialab", "medialab_2024")); // Add admin user directly to the instance's list
            saveUserList(); // Save the updated list without needing to pass it as an argument
        }
    }

    public void printUsers() {
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
    public void clearUserList() {
        this.users.clear(); /// Clear the instance's 'users' list directly
        saveUserList(); //Save the empty list back to the file
    }

    public void deleteUser(User user) {
        BorrowingRecordManager.getInstance().deleteUserLoans(user.getIdNumber());
        users.removeIf(b -> b.getIdNumber().equals(user.getIdNumber()));
        saveUserList(); // Ensure this method writes the updated list to the file
    }

    //updating user when its been edited
    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getIdNumber().equals(updatedUser.getIdNumber())) {
                users.set(i, updatedUser);
                saveUserList();
                break;
            }
        }
    }


}

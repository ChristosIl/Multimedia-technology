package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


public class HelloController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel; // Reference to the error label
    @FXML
    private Label errorLabel_2;

    @FXML
    protected void handleLoginAction() {
        // Reset error messages at the beginning of the method
        errorLabel.setText("");
        errorLabel_2.setText("");

        String usernameText = username.getText();
        String passwordText = password.getText();

        // Check if either the username or password fields are empty
        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            // Display message prompting the user to fill in all fields
            if (usernameText.isEmpty()) {
                errorLabel_2.setText("You must fill up this field");
            }
            if (passwordText.isEmpty()) {
                errorLabel.setText("You must fill up this field");
            }
        } else {
            // Both fields are filled, proceed with login verification
            System.out.println("Username: " + usernameText + ", Password: " + passwordText);

            // Load the user list
            List<User> users = UserDataManager.loadUserList();

            // Check if the user is not the admin and their credentials exist
            if (!usernameText.equalsIgnoreCase("admin")) {
                boolean credentialsExist = false;

                for (User user : users) {
                    if (user.getUsername().equalsIgnoreCase(usernameText) && user.getPassword().equals(passwordText)) {
                        credentialsExist = true;
                        break;
                    }
                }

                if (credentialsExist) {
                    // Login is successful, redirect to the user dashboard
                    System.out.println("Login successfully");

                    try {
                        // Load the User Dashboard view
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Dashboard.fxml"));
                        Parent root = loader.load();

                        // Get the current stage (window) using the username field
                        Stage stage = (Stage) username.getScene().getWindow();

                        // Set the new scene to the stage and show it
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace(); // Handle the IOException
                    }
                } else {
                    // Login failed due to incorrect credentials
                    errorLabel.setText("Wrong credentials");
                }
            } else {
                // If the username is "admin", handle admin login (you can customize this)
                if ("admin".equals(passwordText)) {
                    // Admin login is successful (password is "admin"), you can customize this
                    System.out.println("Admin login successfully");

                    try {
                        // Load the Admin Dashboard view
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                        Parent root = loader.load();

                        // Get the current stage (window) using the username field
                        Stage stage = (Stage) username.getScene().getWindow();

                        // Set the new scene to the stage and show it
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace(); // Handle the IOException
                    }
                } else {
                    // Admin login failed due to incorrect password
                    errorLabel.setText("Wrong credentials");
                }
            }
        }
    }



    @FXML
    protected void handleSignUpAction () {
        try {
            //Load the sign-up page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpView.fxml"));
            Parent root = loader.load();

            //Get the current window (stage) from any component, here using the username TextField
            Stage stage = (Stage) username.getScene().getWindow();

            // Optionally, create a new stage if you want the sign-up form to open in a new window
            // Stage stage = new Stage();

            // Set the sign-up scene to the stage and show it
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
        }
    }
}
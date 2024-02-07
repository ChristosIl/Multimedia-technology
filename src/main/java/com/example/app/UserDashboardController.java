package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDashboardController {

    @FXML
    private Label welcomeLabel; // Reference to the welcome label

    // Method to update the welcome text
    public void setWelcomeText(String text) {
        welcomeLabel.setText(text);
    }

    @FXML
    private void handleLogoutAction() throws IOException {
        // Load the login page FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        // Get the current window (stage) from the welcome label
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();

        // Set the login scene to the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

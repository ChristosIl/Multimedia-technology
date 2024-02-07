package com.example.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;



public class SignUpController {
    @FXML
    private Label SignUp; // Reference to the welcome label

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField Name;
    @FXML
    private TextField Surname;
    @FXML
    private TextField Id_number;
    @FXML
    private TextField email;
    @FXML
    private Label usernameErrorLabel;
    @FXML
    private Label idNumberErrorLabel;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label passwordLabel, nameLabel, surnameLabel;


    //Method to show the SignUpText
    public void setSignUp(String text) {
        SignUp.setText(text);
    }

    @FXML
    private void handleGoBackAction() throws IOException {
        // Load the login page FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        //Get the current window (stage) from the welcome label
        Stage stage = (Stage) SignUp.getScene().getWindow();

        // Set the login scene to the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handlerSigningUp() {
        clearErrorLabels();
        boolean allFieldsValid = validateFields();

        if (!allFieldsValid) {
            return; //Stop the sign-up process if any field is invalid
        }

        List<User> users = UserDataManager.loadUserList();
        boolean usernameExists = false, idNumberExists = false, emailExists = false;

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username.getText())) {
                usernameExists = true;
            }
            if (user.getIdNumber().equals(Id_number.getText())) {
                idNumberExists = true;
            }
            if (user.getEmail().equalsIgnoreCase(email.getText())) {
                emailExists = true;
            }
        }

        if (!usernameExists && !idNumberExists && !emailExists) {
            users.add(new User(username.getText(), password.getText(), Name.getText(), Surname.getText(), Id_number.getText(), email.getText()));
            UserDataManager.saveUserList(users);
            // Clear form fields and display success message or navigate
            clearFormFields();
        } else {
            if (usernameExists) {
                usernameErrorLabel.setText("Username already in use.");
            }
            if (idNumberExists) {
                idNumberErrorLabel.setText("ID number already in use.");
            }
            if (emailExists) {
                emailErrorLabel.setText("Email address already in use.");
            }
        }
    }

    private void clearErrorLabels() {
        usernameErrorLabel.setText("");
        idNumberErrorLabel.setText("");
        emailErrorLabel.setText("");
        passwordLabel.setText("");
        nameLabel.setText("");
        surnameLabel.setText("");
    }

    private boolean validateFields() {
        boolean allFieldsValid = true;

        //Check if username is empty
        if (username.getText().isEmpty()) {
            usernameErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        //Check if password is empty
        if (password.getText().isEmpty()) {
            passwordLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        //Check if Name is empty
        if (Name.getText().isEmpty()) {
            nameLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        //Check if Surname is empty
        if (Surname.getText().isEmpty()) {
            surnameLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        //Check if Id_number is empty
        if (Id_number.getText().isEmpty()) {
            idNumberErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        //Validate email format
        String emailText = email.getText();
        if (emailText.isEmpty() || !emailText.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            emailErrorLabel.setText("Invalid email format");
            allFieldsValid = false;
        }

        return allFieldsValid;
    }


    private void clearFormFields() { //TODO
        // Clear all form fields after successful registration
    }

    private void loadScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) SignUp.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

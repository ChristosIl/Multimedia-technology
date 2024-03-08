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
    private Label SignUp;

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



    @FXML
    private void handleGoBackAction() throws IOException {
        //Load the login page FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) SignUp.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handlerSigningUp() throws IOException {
        clearErrorLabels();
        boolean allFieldsValid = validateFields();

        if (!allFieldsValid) {
            return; //Stop the sign-up process if any field is invalid
        }

        List<User> users = UserDataManager.getInstance().loadUserList();
        boolean usernameExists = false, idNumberExists = false, emailExists = false;

        //Checking for already existing user
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

        //if not we create a new one
        if (!usernameExists && !idNumberExists && !emailExists) {
            User newUser = new User(username.getText(), password.getText(), Name.getText(), Surname.getText(), Id_number.getText(), email.getText());
            UserDataManager.getInstance().getUsers().add(newUser);
            UserDataManager.getInstance().saveUserList();
            clearForm();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Dashboard.fxml"));
            Parent root = loader.load();

            UserDashboardController dashboardController = loader.getController();
            dashboardController.setCurrentUser(newUser);

            Stage stage = (Stage) SignUp.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
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

        //clearForm();
    }

    private void clearErrorLabels() {
        usernameErrorLabel.setText("");
        idNumberErrorLabel.setText("");
        emailErrorLabel.setText("");
        passwordLabel.setText("");
        nameLabel.setText("");
        surnameLabel.setText("");
    }

    //Checking if fields are empty
    private boolean validateFields() {
        boolean allFieldsValid = true;


        if (username.getText().isEmpty()) {
            usernameErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        if (password.getText().isEmpty()) {
            passwordLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        if (Name.getText().isEmpty()) {
            nameLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        if (Surname.getText().isEmpty()) {
            surnameLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        if (Id_number.getText().isEmpty()) {
            idNumberErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        //Checking for correct email format
        String emailText = email.getText();
        if (emailText.isEmpty() || !emailText.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            emailErrorLabel.setText("Invalid email format");
            allFieldsValid = false;
        }

        return allFieldsValid;
    }


    //clear the Text fields /---probably not needed here, but we do it---/
    private void clearForm(){
            username.clear();
            password.clear();
            Name.clear();
            Surname.clear();
            Id_number.clear();
            email.clear();
    }

}

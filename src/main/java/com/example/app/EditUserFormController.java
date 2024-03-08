package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditUserFormController {

    private User currentUser;

    @FXML
    private TextField username, nameField, password, surname, idnumberField, email;

    @FXML
    private Label usernameErrorLabel, idNumberErrorLabel, passwordLabel, nameLabel, surnameLabel, emailErrorLabel;

    //initialize data from Userlistcontroller
    public void initData(User user) {
        this.currentUser = user;
        //Setting up the information for the form fields with user data
        username.setText(user.getUsername());
        nameField.setText(user.getName());
        password.setText(user.getPassword());
        surname.setText(user.getSurname());
        idnumberField.setText(user.getIdNumber());
        email.setText(user.getEmail());
    }

    @FXML
    private void handleGoBackAction() throws IOException {
        //We load the fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserList.fxml"));
        Parent root = loader.load();


        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("List of Users");
        stage.show(); //show if not visible
    }

    @FXML
    private void handleSaveChangesAction() throws IOException{
        boolean allFieldsValid = validateFields();

        if (!allFieldsValid) {
            return; //Stop the sign-up process if any field is invalid
        }
        //We update the currentUser object with new values from form fields
        currentUser.setUsername(username.getText());
        currentUser.setPassword(password.getText());
        currentUser.setName(nameField.getText());
        currentUser.setSurname(surname.getText());
        currentUser.setIdNumber(idnumberField.getText());
        currentUser.setEmail(email.getText());

        //We call UserDataManager and update the user list and then we save it
        UserDataManager.getInstance().updateUser(currentUser);

        switchToUserListView(); //save and go back
    }

    private void switchToUserListView() throws IOException {
        //Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserList.fxml"));
        Parent userListRoot = loader.load();

        //we pass the changes back
        UserListController controller = loader.getController();
        controller.refreshUserList();

        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(new Scene(userListRoot));
        stage.show();
    }

    //checking for empty fields
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

        if (nameField.getText().isEmpty()) {
            nameLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        if (surname.getText().isEmpty()) {
            surnameLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        if (idnumberField.getText().isEmpty()) {
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
}

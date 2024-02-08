package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditUserFormController {

    private User currentUser;

    @FXML
    private TextField username, nameField, password, surname, idnumberField, email;

    //initialize data from Uselistcontroller
    public void initData(User user) {
        this.currentUser = user;
        // Initialize form fields with user data
        username.setText(user.getUsername());
        nameField.setText(user.getName());
        password.setText(user.getPassword());
        surname.setText(user.getSurname());
        idnumberField.setText(user.getIdNumber());
        email.setText(user.getEmail());
        // Initialize other fields in a similar manner...
    }

    @FXML
    private void handleGoBackAction() throws IOException {
        // Load the FXML file for the scene you want to switch to
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserList.fxml")); // Update the path accordingly
        Parent root = loader.load();

        // Get the current stage using the scene associated with any control
        Stage stage = (Stage) username.getScene().getWindow();

        // Set the scene to the stage
        stage.setScene(new Scene(root));

        // (Optional) Set the title of the stage
        stage.setTitle("List of Users");

        // Show the stage if not already visible
        stage.show();
    }

    @FXML
    private void handleSaveChangesAction() throws IOException{
        // Update the currentUser object with new values from form fields
        currentUser.setUsername(username.getText());
        currentUser.setPassword(password.getText());
        currentUser.setName(nameField.getText());
        currentUser.setSurname(surname.getText());
        currentUser.setIdNumber(idnumberField.getText());
        currentUser.setEmail(email.getText());

        // Call UserDataManager to update the user list and save it
        UserDataManager.getInstance().updateUser(currentUser);


        switchToUserListView();
    }

    private void switchToUserListView() throws IOException {
        // Load the FXML file for the user list scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserList.fxml"));
        Parent userListRoot = loader.load();


        UserListController controller = loader.getController();
        controller.refreshUserList();

        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(new Scene(userListRoot));
        stage.show();
    }
}

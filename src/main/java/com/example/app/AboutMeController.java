package com.example.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutMeController {
    //TODO: FIX THE ABOUT ME PAGE

    @FXML
    private Button GoBackButton;
    private User currentUser;
    @FXML
    private TextField username, idnumberField, nameField, emailField, surnameField;


    @FXML
    private void handlegobackAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Dashboard.fxml"));
        Parent root = loader.load();

        UserDashboardController dashboardController = loader.getController();
        dashboardController.setCurrentUser(currentUser);


        Stage stage = (Stage) GoBackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateTextFields();
    }

    public void updateTextFields(){
        if(currentUser != null ){
            username.setText(currentUser.getUsername());
            idnumberField.setText(currentUser.getIdNumber());
            nameField.setText(currentUser.getName());
            emailField.setText(currentUser.getEmail());
            surnameField.setText(currentUser.getSurname());
        }
    }


}

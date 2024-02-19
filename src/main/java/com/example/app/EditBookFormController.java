package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditBookFormController {
    private Book book;

    @FXML
    private Button GoBackButton;
    @FXML
    private TextField titleTextField;
    @FXML
    private void handlegobackAction() throws IOException {
        // Load the login page FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) GoBackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void setBook(Book book) {
        this.book = book;
        // Populate form fields with book details
        titleTextField.setText(book.getTitle());
        // Repeat for other fields
    }
}

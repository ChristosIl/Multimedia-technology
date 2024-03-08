package com.example.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditBookFormController {
    private Book book;

    @FXML
    private Button GoBackButton, EditButton;
    @FXML
    private TextField titleTextField,authorTextField,isbnTextField,yearofpublishingTextField,categoryTextField,numberofcopiesTextField, publisherTextField ;
    @FXML
    private Label titleErrorLabel, authorErrorLabel, isbnErrorLabel, yearofpublishingErrorLabel, categoryErrorLabel, numberofcopiesErrorLabel, editedLabel, publisherErrorLabel;
    @FXML
    private void handlegobackAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml")); //Loading the login page FXML
        Parent root = loader.load();

        Stage stage = (Stage) GoBackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void setBook(Book book) {
        this.book = book;

        //Populate form fields with book details
        titleTextField.setText(book.getTitle());
        authorTextField.setText(book.getAuthor());
        isbnTextField.setText(book.getIsbn());
        yearofpublishingTextField.setText(String.valueOf(book.getYearOfPublishing()));
        categoryTextField.setText(book.getCategory().getName());
        numberofcopiesTextField.setText(String.valueOf(book.getNumberOfCopies()));
        publisherTextField.setText(book.getPublishingHouse());
    }

    @FXML
    private void handleEditAction() throws IOException{
        clearErrorLabels();
        boolean allFieldsValid = validateFields();

        if(!allFieldsValid){
            return;
        }

        String newIsbn = isbnTextField.getText();
        if (!book.getIsbn().equals(newIsbn) && BookManager.getInstance().isbnExists(newIsbn, book.getIsbn())) {
            isbnErrorLabel.setText("ISBN is already in use by another book.");
            return;
        }

        book.setTitle(titleTextField.getText());
        book.setAuthor(authorTextField.getText());
        book.setIsbn(isbnTextField.getText());
        book.setPublishingHouse(publisherTextField.getText());
        try{
            book.setYearOfPublishing(Integer.parseInt(yearofpublishingTextField.getText()));
            book.setNumberOfCopies(Integer.parseInt(numberofcopiesTextField.getText()));
        } catch (NumberFormatException e){
            System.err.println("Error: Invalid number format");
            yearofpublishingErrorLabel.setText("Invalid number");
            numberofcopiesErrorLabel.setText("Invalid number");
            return;
        }

        Category newCategory = CategoryManager.getInstance().findOrCreateCategory(categoryTextField.getText());
        book.setCategory(newCategory);

        BookManager.getInstance().updateBook(book);

        editedLabel.setText("Changed");
    }

    private boolean validateFields() {
        boolean allFieldsValid = true;

        if(titleTextField.getText().isEmpty()){
            titleErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }
        if(authorTextField.getText().isEmpty()){
            authorErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }
        if(isbnTextField.getText().isEmpty()){
            isbnErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }
        if(yearofpublishingTextField.getText().isEmpty()){
            yearofpublishingErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }
        if(numberofcopiesTextField.getText().isEmpty()){
            numberofcopiesErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }
        if(publisherTextField.getText().isEmpty()){
            publisherErrorLabel.setText("This field is empty");
            allFieldsValid = false;
        }

        return allFieldsValid;
    }

    private void clearErrorLabels() {
        //We clear all error labels
        titleErrorLabel.setText("");
        authorErrorLabel.setText("");
        isbnErrorLabel.setText("");
        yearofpublishingErrorLabel.setText("");
        categoryErrorLabel.setText("");
        numberofcopiesErrorLabel.setText("");
        editedLabel.setText("");

    }
}

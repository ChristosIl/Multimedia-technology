package com.example.app;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.io.*;


public class AddBookController {
    private BookListener bookListener;

    @FXML
    private TextField titleField, authorField, isbnField, yearOfPublishingField, categoryField, numberOfCopiesField,publisherField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button goBackButton;

    private BookManager bookManager = BookManager.getInstance(); //singleton pattern

    @FXML
    private void handleAddBookAction(ActionEvent event) {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String categoryName = categoryField.getText();
        String yearStr = yearOfPublishingField.getText();
        String copiesStr = numberOfCopiesField.getText();
        String publisher = publisherField.getText();
        if (!validateInput(yearStr, copiesStr)) {
            messageLabel.setText("Invalid input. Please enter integers for year and copies.");
            return;
        }

        int yearOfPublishing = Integer.parseInt(yearStr);
        int numberOfCopies = Integer.parseInt(copiesStr);

        CategoryManager categoryManager = CategoryManager.getInstance(); //singleton pattern
        Category category = categoryManager.findOrCreateCategory(categoryName);
        categoryManager.saveCategories();
        //Checking existing books by ISBN
        if (bookManager.getBooks().stream().anyMatch(book -> book.getIsbn().equals(isbn))) {
            messageLabel.setText("The book already exists.");
            return;
        }

        Book newBook = new Book(title, author, publisher , isbn, yearOfPublishing, category, numberOfCopies);
        bookManager.addBook(newBook); //adding the book to the list of books

        if (bookListener != null) {
            bookListener.onBookUpdated();
        }
        messageLabel.setText("Book added successfully.");
        clearForm();


    }

    private boolean validateInput(String year, String copies) {
        try {
            Integer.parseInt(year);
            Integer.parseInt(copies);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void clearForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        yearOfPublishingField.clear();
        categoryField.clear();
        numberOfCopiesField.clear();
        publisherField.clear();
    }

    //"Go Back" button handler
    @FXML
    private void handleGoBackAction(ActionEvent event) {
        Stage stage = (Stage) goBackButton.getScene().getWindow();
        stage.close();
    }

    public void setBookListener(BookListener listener) {
        this.bookListener = listener;
    }
}

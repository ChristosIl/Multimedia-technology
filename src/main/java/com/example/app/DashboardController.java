package com.example.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DashboardController {

    @FXML
    private Label welcomeLabel; // Reference to the welcome label
    @FXML
    private MenuBar MenuBar;
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> yearofpublishingColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, String> copiesColumn;
    // Method to update the welcome text
    public void setWelcomeText(String text) {
        welcomeLabel.setText(text);
    }

    @FXML
    private void handleSeeTheListAction() {
        // Toggle visibility and managed state of the booksTable
        boolean isVisible = booksTable.isVisible();
        booksTable.setVisible(!isVisible);
        booksTable.setManaged(!isVisible);

        // If you're loading the books dynamically when the menu item is clicked,
        // make sure to call loadBooks() here if the table is being made visible.
        if (!isVisible) {
            booksTable.setItems(loadBooks()); // Assuming loadBooks() loads your book data
        }
    }

    @FXML
    public void initialize() {
        //Set up the CellValueFactory for each TableColumn
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        yearofpublishingColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfPublishing"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCopies"));

        // Example: Load books into the table
        // This step assumes you have a method to load books from your data source
        booksTable.setItems(loadBooks()); // Ensure you implement loadBooks() to return ObservableList<Book>
    }

    @FXML
    private void handleAddNewBookAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBookForm.fxml"));
        Parent root = loader.load();

        // Create a new scene with the loaded page
        Scene scene = new Scene(root);

        // Create a new stage for the popup
        Stage stage = new Stage();
        stage.setTitle("Add New Book");
        stage.setScene(scene);

        // Show the new stage
        stage.show();
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

    //loadbooks
    private ObservableList<Book> loadBooks() {
        // Access the singleton instance of BookManager
        BookManager bookManager = BookManager.getInstance();

        // Convert the List<Book> to ObservableList<Book> for use with TableView
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(bookManager.getBooks());

        return observableBooks;
    }
}

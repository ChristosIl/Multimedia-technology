package com.example.app;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class UsersBooksOnLoanController {
    @FXML
    private Button GoBackButton;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<BorrowingRecord, String> titleColumn, authorColumn, isbnColumn;
    @FXML
    private TableColumn<BorrowingRecord, LocalDate> borrowdateColumn, returndateColumn;

    private String userId;
    private User currentUser;

    @FXML
    public void initialize(){
        setupColumnValueFactories();
        populateTableView();
        setupContextMenu();
    }

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

    private void setupColumnValueFactories() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("bookIsbn"));
        borrowdateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returndateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }

    private void populateTableView() {
       if(currentUser != null){
           List<BorrowingRecord> borrowingRecords = BorrowingRecordManager.getInstance().getRecordsByUserId(currentUser.getIdNumber());
           ObservableList<BorrowingRecord> observableBorrowingRecords = FXCollections.observableArrayList(borrowingRecords);
           tableView.setItems(observableBorrowingRecords);
       }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        populateTableView(); // Call this here to refresh the table view based on the user ID
    }

    private void setupContextMenu() {
        // Create the context menu and return option
        ContextMenu contextMenu = new ContextMenu();


        tableView.setRowFactory(tv -> {
            TableRow<BorrowingRecord> row = new TableRow<>();
            ContextMenu rowMenu = new ContextMenu();
            MenuItem returnItem = new MenuItem("Return this book");
            MenuItem rateMenuItem = new MenuItem("Rate this book");


            returnItem.setOnAction(event -> {
                BorrowingRecord selectedRecord = row.getItem();
                if (selectedRecord != null) {
                    returnBook(selectedRecord);
                    System.out.println("Return action triggered for: " + selectedRecord.getBookIsbn());
                }
            });


            rateMenuItem.setOnAction(event -> {
                BorrowingRecord selectedRecord = row.getItem();
                if (selectedRecord != null) {
                    showRatingDialog(selectedRecord);
                }
            });

            rowMenu.getItems().addAll(returnItem, rateMenuItem);

            // Only display context menu for non-null items.
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));
            return row;
        });

    }

    private void returnBook(BorrowingRecord record) {
        BorrowingRecordManager.getInstance().returnBook(currentUser.getIdNumber(), record.getBookIsbn());
        populateTableView(); // Refresh the list of borrowed books

        // Show confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle("Return Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("The book has been successfully returned.");
        confirmationAlert.showAndWait();
    }

    private void showRatingDialog(BorrowingRecord record) {
        // Create a custom dialog
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Rate This Book");
        dialog.setHeaderText("Select your rating for the book");

        // Set the button types.
        ButtonType rateButtonType = new ButtonType("Rate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(rateButtonType, ButtonType.CANCEL);

        // Create a ChoiceBox for ratings
        ChoiceBox<Integer> ratingChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        ratingChoiceBox.setValue(5); // Default to highest rating

        dialog.getDialogPane().setContent(ratingChoiceBox);

        // Convert the result to a rating when the rate button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == rateButtonType) {
                return ratingChoiceBox.getValue();
            }
            return null;
        });

        Optional<Integer> result = dialog.showAndWait();

        result.ifPresent(rating -> {
            System.out.println("Rating: " + rating); // For debugging
            updateBookRating(record, rating);
        });
    }

    private void updateBookRating(BorrowingRecord record, int rating) {
        // Assuming BookManager can access and update books by ISBN
        Book book = BookManager.getInstance().getBookByIsbn(record.getBookIsbn());
        if (book != null) {
            book.addRating(rating); // Assuming addRating(int rating) updates the book's rating
            BookManager.getInstance().saveBooks(); // Assuming saveBooks() method exists to persist changes
        }
    }

}

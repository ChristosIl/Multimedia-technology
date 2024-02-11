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
        MenuItem returnMenuItem = new MenuItem("Return");
        contextMenu.getItems().add(returnMenuItem);

        tableView.setRowFactory(tv -> {
            TableRow<BorrowingRecord> row = new TableRow<>();
            ContextMenu rowMenu = new ContextMenu();
            MenuItem returnItem = new MenuItem("Return this book");

            returnItem.setOnAction(event -> {
                BorrowingRecord selectedRecord = row.getItem();
                if (selectedRecord != null) {
                    returnBook(selectedRecord);
                    System.out.println("Return action triggered for: " + selectedRecord.getBookIsbn());
                }
            });
            rowMenu.getItems().addAll(returnItem);

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

}

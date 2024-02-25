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

public class BorrowedBooksListController {

    @FXML
    private Button GoBackButton;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<BorrowingRecord, String> titleColumn, authorColumn, isbnColumn, idNumberColumn;
    @FXML
    private TableColumn<BorrowingRecord, LocalDate> borrowdateColumn, returndateColumn;

    @FXML
    public void initialize(){
        setupColumnValueFactories();
        populateTableView();
        setupContextMenu();
    }
    @FXML
    private void handlegobackAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) GoBackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void setupColumnValueFactories() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("bookIsbn"));
        idNumberColumn.setCellValueFactory(new PropertyValueFactory<>("userIdNumber"));
        borrowdateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returndateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }
    private void populateTableView() {
        List<BorrowingRecord> borrowingRecords = BorrowingRecordManager.getInstance().getBorrowingRecords();
        ObservableList<BorrowingRecord> observableBorrowingRecords = FXCollections.observableArrayList(borrowingRecords);
        tableView.setItems(observableBorrowingRecords);
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem terminateLoanItem = new MenuItem("Terminate Loan");
        contextMenu.getItems().add(terminateLoanItem);

        tableView.setRowFactory(tv -> {
            TableRow<BorrowingRecord> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu) null));

            terminateLoanItem.setOnAction(event -> {
                System.out.println("Terminate Loan clicked"); // Debugging print
                BorrowingRecord selectedRecord = (BorrowingRecord)tableView.getSelectionModel().getSelectedItem();
                if (selectedRecord != null) {
                    System.out.println("Selected record for termination: " + selectedRecord.getBookIsbn());

                    // Show confirmation dialog
                    boolean confirmation = showConfirmationDialog(
                            "Terminate Loan Confirmation",
                            "Are you sure you want to terminate the loan for the selected book?");

                    if (confirmation) {
                        terminateLoan(selectedRecord);
                    } else {
                        System.out.println("Loan termination cancelled.");
                    }
                } else {
                    System.out.println("No record selected");
                }
            });
            return row;
        });
    }

    private void terminateLoan(BorrowingRecord record) {
        BorrowingRecordManager.getInstance().removeRecord(record.getUserIdNumber(), record.getBookIsbn());
        //BookManager.getInstance().increaseBookCopies(record.getBookIsbn());
        populateTableView(); //Refresh the TableView with the updated list
    }


    private boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}

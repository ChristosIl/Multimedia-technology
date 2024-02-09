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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
    }
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

}

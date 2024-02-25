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

public class AboutMeController {
    //TODO: FIX THE ABOUT ME PAGE

    @FXML
    private Button GoBackButton;
    private User currentUser;
    @FXML
    private TableView<Book> ratingTableView;
    @FXML
    private TableColumn<Book, String> titleColumn, authorColumn, isbnColumn;
    @FXML
    private TableColumn<Book, Integer> ratingColumn;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("ratings"));

        populateRatingTableView();
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

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private void populateRatingTableView() {
        ObservableList<Book> books = FXCollections.observableArrayList(BookManager.getInstance().getBooks());
        ratingTableView.setItems(books);
    }
}

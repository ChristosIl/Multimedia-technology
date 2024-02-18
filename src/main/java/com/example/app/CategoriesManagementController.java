package com.example.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.app.Category;

import java.io.IOException;
import java.util.List;

public class CategoriesManagementController {
    @FXML
    private Button GoBackButton;
    @FXML
    private TableView<Category> categoriesTable;
    @FXML
    private TableColumn<Category, String> nameColumn;

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

    public void initialize() {
        CategoryManager categoryManager = CategoryManager.getInstance();
        List<Category> categories = categoryManager.getCategories();
        ObservableList<Category> observableCategories = FXCollections.observableArrayList(categories);
        categoriesTable.setItems(observableCategories);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(column -> new TableCell<Category, String>() {
            @Override
            protected void updateItem(String categoryName, boolean empty) {
                super.updateItem(categoryName, empty);
                if (categoryName == null || empty) {
                    setText(null);
                } else {
                    // Assuming CategoryManager has a method to count books by category name
                    int bookCount = BookManager.getInstance().getBookCountByCategory(categoryName);
                    setText(categoryName + " (" + bookCount + ")");
                }
            }
        });
    }


}

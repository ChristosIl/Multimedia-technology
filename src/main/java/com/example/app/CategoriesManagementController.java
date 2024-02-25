package com.example.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.example.app.Category;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CategoriesManagementController {
    @FXML
    private Button GoBackButton, addnewcategoryButton;
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

    @FXML
    private void handleAddNewCategoryAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Category");
        dialog.setHeaderText("Create New Category");
        dialog.setContentText("Please enter the new category name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.trim().isEmpty() && CategoryManager.getInstance().getCategories().stream().noneMatch(c -> c.getName().equalsIgnoreCase(name.trim()))) {
                //create new category
                Category newCategory = new Category(name.trim());
                CategoryManager.getInstance().addCategory(newCategory.getName());
                refreshTableView();
            } else {
                //Invalid category or already exists
                Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid or duplicate category name.", ButtonType.OK);
                alert.showAndWait();
            }
        });
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
                    //count the books that belong to a specific category
                    int bookCount = BookManager.getInstance().getBookCountByCategory(categoryName);
                    setText(categoryName + " (" + bookCount + ")");
                }
            }
        });

        //right click
        categoriesTable.setRowFactory(tv -> {
            TableRow<Category> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction(event -> {
                Category selectedCategory = row.getItem();
                showEditDialog(selectedCategory);
            });

            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(event -> {
                Category selectedCategory = row.getItem();
                categoryManager.deleteCategory(selectedCategory.getName());
                observableCategories.remove(selectedCategory);
                refreshTableView();
            });

            contextMenu.getItems().addAll(editItem, deleteItem);

            //Only display context menu for non-null items
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(javafx.beans.binding.Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu)null));

            return row;
        });
    }

    private void showEditDialog(Category category) {
        TextInputDialog dialog = new TextInputDialog(category.getName());
        dialog.setTitle("Edit Category");
        dialog.setHeaderText("Change Category Name");
        dialog.setContentText("Please enter the new category name:");


        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            //We check if the new name is not empty, not the same as the old one, and not already used
            if (!newName.trim().isEmpty() && !category.getName().equalsIgnoreCase(newName.trim()) &&
                    CategoryManager.getInstance().getCategories().stream().noneMatch(c -> c.getName().equalsIgnoreCase(newName.trim()))) {
               //update
                CategoryManager.getInstance().editCategory(category.getName(), newName.trim());
                refreshTableView();
            }
        });
    }

    private void refreshTableView() {
        categoriesTable.getItems().clear();
        categoriesTable.getItems().addAll(CategoryManager.getInstance().getCategories());
    }


}

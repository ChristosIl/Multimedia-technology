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
import java.util.List;

public class UserListController {
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> surnameColumn;
    @FXML
    private TableColumn<User, String> idnumberColumn;
    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        idnumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Load users into the table
        usersTable.setItems(loadUsers());

        //context menu to delete book
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete user");
        MenuItem editItem = new MenuItem("Edit");
        contextMenu.getItems().addAll(deleteItem, editItem);

        //Action for delete context menu
        deleteItem.setOnAction(event -> {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                deleteUser(selectedUser);
            }
        });

        editItem.setOnAction(event -> {
            User selectedUser = usersTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("EditUserForm.fxml"));
                    Parent root = loader.load();

                    EditUserFormController controller = loader.getController();
                    controller.initData(selectedUser); // Method to pass data to the edit form

                    Scene scene = new Scene(root);
                    Stage stage = (Stage) usersTable.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Edit User");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Attach the context menu to the table rows
        usersTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu) null)); // Ensures the context menu is shown only for non-empty rows.
            return row;
        });

    }


    private ObservableList<User> loadUsers() {
        // Initialize the ObservableList
        ObservableList<User> users = FXCollections.observableArrayList();

        // Assuming you have a UserManager or similar class that can return all registered users
        List<User> registeredUsers = UserDataManager.getInstance().getUsers();

        // Add all registered users to the ObservableList
        users.addAll(registeredUsers);

        return users;
    }

    private void deleteUser(User user) {
        // Correctly calling deleteBook on BookManager's instance
        UserDataManager.getInstance().deleteUser(user);
        // Refresh the TableView with the updated list
        usersTable.setItems(FXCollections.observableArrayList(UserDataManager.getInstance().getUsers()));
    }

    public void refreshUserList() {
        ObservableList<User> userList = FXCollections.observableArrayList(UserDataManager.getInstance().getUsers());
        usersTable.setItems(userList);
    }
}

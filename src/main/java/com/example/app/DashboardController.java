package com.example.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TableView;

public class DashboardController implements BookListener{

    @FXML
    private AnchorPane rootPane;
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
    @FXML
    private TableColumn<Book, String> publisherColumn;
    // Method to update the welcome text
    public void setWelcomeText(String text) {
        welcomeLabel.setText(text);
    }

    @Override
    public void onBookUpdated() {
        loadCss();
        booksTable.setItems(FXCollections.observableArrayList(BookManager.getInstance().getBooks()));
    }
    //see the list of books
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

    //see the list of Users
    @FXML
    private void handleSeeUsersAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserList.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Users List");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.NONE); //non modal window
        stage.showAndWait();
    }

    @FXML
    public void initialize() {
        loadCss();
        //Set up the CellValueFactory for each TableColumn
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        yearofpublishingColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfPublishing"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCopies"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publishingHouse"));
        //load the books
        booksTable.setItems(loadBooks());

        //context menu to delete book
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete Book");
        MenuItem editItem = new MenuItem("Edit");
        MenuItem commentItem = new MenuItem("View Comments and Rating");
        contextMenu.getItems().addAll(deleteItem, editItem, commentItem);

        //Action for delete context menu
        deleteItem.setOnAction(event -> {
            Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                deleteBook(selectedBook);
            }
        });
        //Action for Edit context menu
        editItem.setOnAction(event -> {
            Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                showEditBookForm(selectedBook);
            }
        });

        //Attach the context menu to the table rows
        booksTable.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });

        commentItem.setOnAction(event -> {
            Book selectedBook = (Book) booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                showBookDetails(selectedBook);
            }
        });
    }

    private void loadCss() {
        String css = this.getClass().getResource("/com/example/app/css/adminpage.css").toExternalForm();
        rootPane.getStylesheets().add(css);
    }
    @FXML
    private void handleAddNewBookAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBookForm.fxml"));
        Parent root = loader.load();

        AddBookController addBookController = loader.getController();
        addBookController.setBookListener(this);

        // Create a new scene with the loaded page
        Scene scene = new Scene(root);
        // Create a new stage for the popup
        Stage stage = new Stage();
        stage.setTitle("Add New Book");
        stage.setScene(scene);
        //Show the new stage
        stage.show();
    }

    @FXML
    private void handleseeborrowedbookslist() throws IOException{
        try {
            //Load the sign-up page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BorrowedBooksList.fxml"));
            Parent root = loader.load();

            //Get the current window (stage) from any component(here i chose the username TextField)
            Stage stage = (Stage) booksTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCategoriesManagement() throws IOException{
        try {
            //Load the sign-up page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CategoriesManagement.fxml"));
            Parent root = loader.load();
            //Get the current window (stage) from any component(here i chose the username TextField)
            Stage stage = (Stage) booksTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogoutAction() throws IOException {
        //Load the login page FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        //Get the current window (stage) from the welcome label
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        //Set the login scene to the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //loadbooks
    private ObservableList<Book> loadBooks() {
        //Access the singleton instance of BookManager
        BookManager bookManager = BookManager.getInstance();
        // Convert the List<Book> to ObservableList<Book> for use with TableView
        ObservableList<Book> observableBooks = FXCollections.observableArrayList(bookManager.getBooks());


        return observableBooks;
    }

    private void deleteBook(Book book) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Book");
        confirmAlert.setContentText("Are you sure you want to delete this book?");

        ButtonType buttonTypeDelete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmAlert.getButtonTypes().setAll(buttonTypeDelete, buttonTypeCancel);

        //dialog display
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if(result.isPresent() && result.get() == buttonTypeDelete){
            //clearing all comments
            book.clearComments();
            //calling deleteBook on BookManager's instance
            BookManager.getInstance().deleteBook(book);
            //Refresh the TableView
            booksTable.setItems(FXCollections.observableArrayList(BookManager.getInstance().getBooks()));
        }

         }

    private void showBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminsBookDetails.fxml"));
            Parent root = loader.load();

            AdminsBookDetailsController controller = loader.getController();
            controller.setBook(book);
            Scene currentScene = booksTable.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditBookForm(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditBookForm.fxml"));
            Parent root = loader.load();

            //Get the controller and set the book (passing the book we chose to edit)
            EditBookFormController controller = loader.getController();
            controller.setBook(book);
            //edit book form -> root of the current scene
            Scene currentScene = booksTable.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

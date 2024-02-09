package com.example.app;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserDashboardController {

    @FXML
    public Button logoutButton, searchButton;
    @FXML
    private Label welcomeLabel; // Reference to the welcome label
    @FXML
    private TableView booksTable;
    @FXML
    private TableColumn<Book, String> titleColumn, authorColumn, isbnColumn, yearOfPublishingColumn, categoryColumn, numberOfCopiesColumn;
    @FXML
    private TextField searchField;
    private User currentUser;
    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        yearOfPublishingColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfPublishing"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        numberOfCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCopies"));

        //put books on the table
        populateTableView();

        //Listener to watch the search Field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue));

        //borrow context menu
        ContextMenu contextMenu = new ContextMenu();
        MenuItem borrowItem = new MenuItem("Borrow this book");
        contextMenu.getItems().addAll(borrowItem );

        borrowItem.setOnAction(event -> {

        });

        booksTable.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });

        borrowItem.setOnAction(event -> {
            Book selectedBook = (Book) booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                borrowBook(selectedBook);
            }
            booksTable.refresh();
        });

    }


    //must be deleted TODO
    public void setWelcomeText(String text) {
        welcomeLabel.setText(text);
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
    //-------Set the Welcome text and name
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateDashboard();
    }

    private void updateDashboard() {
        welcomeLabel.setText("Welcome, " + currentUser.getName());
    }

    //-------

    //function to put books on the table
    private void populateTableView() {
        // Populate the TableView with books
        ObservableList<Book> books = FXCollections.observableArrayList(BookManager.getInstance().getBooks());
        booksTable.setItems(books);
    }

    private void filterBooks(String searchText) {
        //--Show all books if empty
        if (searchText.isEmpty()) {
            populateTableView();
            return;
        }
        //i use a list here. should i do that somehow else, because i have to add the BORROW button
        //
        List<Book> filteredBooks = BookManager.getInstance().getBooks().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(searchText.toLowerCase()) ||
                        Integer.toString(book.getYearOfPublishing()).contains(searchText))
                .collect(Collectors.toList());
        booksTable.setItems(FXCollections.observableArrayList(filteredBooks));
    }

    private void borrowBook(Book selectedBook) {
        int activeBorrowings = BorrowingRecordManager.getInstance().countActiveBorrowingsForUser(currentUser.getIdNumber());
        if (activeBorrowings >= 2) {
            //TODO show him a message in the app
            System.out.println("You have reached the limit of 2 borrowed books.");
            return;
        }

        if (selectedBook.getNumberOfCopies() <= 0) {
            //TODO implement this message to be displayed inside the app
            System.out.println("There are no copies of the book available.");
        } else {
            //Decrease book copies and update the list
            BookManager.getInstance().decreaseBookCopies(selectedBook.getIsbn());
            populateTableView();

            //Record the borrowing event
            BorrowingRecord record = new BorrowingRecord(currentUser.getIdNumber(), selectedBook.getIsbn(), LocalDate.now(), selectedBook.getTitle(), selectedBook.getAuthor());
            BorrowingRecordManager.getInstance().addRecord(record);
        }
    }
}

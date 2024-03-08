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
    private Label welcomeLabel;
    @FXML
    private TableView booksTable;
    @FXML
    private TableColumn<Book, String> titleColumn, authorColumn, isbnColumn, yearOfPublishingColumn, categoryColumn, numberOfCopiesColumn, publisherColumn ;
    @FXML
    private TextField searchField;
    private User currentUser;
    @FXML
    public void initialize() {
        //we pass the information of the books
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        yearOfPublishingColumn.setCellValueFactory(new PropertyValueFactory<>("yearOfPublishing"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        numberOfCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfCopies"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publishingHouse"));
        //put books on the table
        populateTableView();

        //Listener to watch the search Field for any changes
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue));

        //Context menu(right click :) )
        ContextMenu contextMenu = new ContextMenu();
        MenuItem borrowItem = new MenuItem("Borrow this book");
        MenuItem viewCommentsItem = new MenuItem("View Comments and Rating");
        contextMenu.getItems().addAll(borrowItem, viewCommentsItem);


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

        viewCommentsItem.setOnAction(event -> {
            Book selectedBook = (Book) booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                showBookDetails(selectedBook);
            }
        });

    }


    @FXML
    private void handleLogoutAction() throws IOException {
        //Load the login page FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        //Get the current window (stage) from the welcome label
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleBookonloanuserslist()throws IOException{
        try {
            //Load the sign-up page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UsersBooksOnLoan.fxml"));
            Parent root = loader.load();

            UsersBooksOnLoanController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            //Get the current window (stage) from any component (here using the username TextField)
            Stage stage = (Stage) booksTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); //catch any exception
        }
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

        //checking if the user borrowed bookes over 2 times (he can't)
        if (activeBorrowings >= 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Borrowing Limit Reached");
            alert.setHeaderText(null);
            alert.setContentText("You have reached the limit of 2 borrowed books.");
            alert.showAndWait();
            System.out.println("You have reached the limit of 2 borrowed books.");
            return;
        }

        //Alert if there are no copies available
        if (selectedBook.getNumberOfCopies() <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Book Unavailable");
            alert.setHeaderText(null);
            alert.setContentText("There are no copies of the book available.");

            alert.showAndWait();
            System.out.println("There are no copies of the book available.");
        } else {
            //If there are more than 0
            //Decrease book copies and update the list
            BookManager.getInstance().decreaseBookCopies(selectedBook.getIsbn());
            populateTableView();

            //Record the borrowing event
            BorrowingRecord record = new BorrowingRecord(currentUser.getIdNumber(), selectedBook.getIsbn(), LocalDate.now(), selectedBook.getTitle(), selectedBook.getAuthor());
            BorrowingRecordManager.getInstance().addRecord(record);
        }
    }

    @FXML
    private void handleAboutMe()throws IOException{
        try {
            //Load the sign-up page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutMe.fxml"));
            Parent root = loader.load();

            AboutMeController controller = loader.getController();
            controller.setCurrentUser(currentUser); //pass the user's information

            Stage stage = (Stage) booksTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookDetails.fxml"));
            Parent root = loader.load();

            BookDetailsController controller = loader.getController();
            controller.setBook(book);
            controller.setCurrentUser(currentUser);

            Scene currentScene = booksTable.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

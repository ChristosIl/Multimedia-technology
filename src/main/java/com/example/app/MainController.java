package com.example.app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class MainController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel; // Reference to the error label
    @FXML
    private Label errorLabel_2;
    @FXML
    private ListView<String> topRatedBooksListView;

    @FXML
    public void initialize() {
        displayTopRatedBooks();
        loadCss();
    }

    //We load the css
    private void loadCss() {
        String css = this.getClass().getResource("/com/example/app/css/style.css").toExternalForm();
        rootPane.getStylesheets().add(css);
    }
    @FXML
    protected void handleLoginAction() throws IOException {

        errorLabel.setText("");
        errorLabel_2.setText("");

        String usernameText = username.getText();
        String passwordText = password.getText();

        //Check if either the username or password fields are empty
        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            //message to fill in the fields
            if (usernameText.isEmpty()) {
                errorLabel_2.setText("You must fill in this field");
            }
            if (passwordText.isEmpty()) {
                errorLabel.setText("You must fill in this field");
            }
        } else {
            List<User> users = UserDataManager.getInstance().getUsers();
            boolean found = false;
            User loggedInUser = null;

            //Admin check
            if (usernameText.equalsIgnoreCase("medialab") && passwordText.equals("medialab_2024")) {
                // Open admin dashboard
                loadDashboard("Dashboard.fxml");
                System.out.println("Login successfully!");
                System.out.println("Admin logged in");
                return;
            }

            //checking which user is going to log in
            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(usernameText) && user.getPassword().equals(passwordText)) {
                    found = true;
                    loggedInUser = user;
                    break;
                }
            }

            if (found && loggedInUser != null) {
                //Proceed to load user's-specific dashboard
                loadUserDashboard(loggedInUser);
                System.out.println("Login successfully!");
                System.out.println("Username: " + usernameText + " Password: " + passwordText);
            } else {
                errorLabel.setText("Wrong credentials");
            }
        }
    }




    @FXML
    protected void handleSignUpAction () {
        try {
            //Load the sign-up page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUpView.fxml"));
            Parent root = loader.load();

            //Get the current window (stage) from any component, here using the username TextField
            Stage stage = (Stage) username.getScene().getWindow();
            //We set the sign-up scene to the stage and we show it
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //User dashboard load
    private void loadUserDashboard(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("User-Dashboard.fxml"));
        Parent root = loader.load();

        UserDashboardController dashboardController = loader.getController();
        dashboardController.setCurrentUser(user); // This method needs to be implemented in UserDashboardController

        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    //admin dashboard load
    private void loadDashboard(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void displayTopRatedBooks() {
        List<Book> books = BookManager.getInstance().getBooks();

        //Sort the books by their average rating in descending order and limit to top 5
        List<Book> topRatedBooks = books.stream()
                .sorted((book1, book2) -> Double.compare(book2.getAverageRating(), book1.getAverageRating()))
                .limit(5)
                .collect(Collectors.toList());

        //Get the titles and average ratings of the top-rated books
        List<String> topRatedBooksInfo = topRatedBooks.stream()
                .map(book -> book.getTitle() + " - Average Rating: " + String.format("%.2f", book.getAverageRating()))
                .collect(Collectors.toList());

        //We display the titles and ratings in the ListView
        topRatedBooksListView.setItems(FXCollections.observableArrayList(topRatedBooksInfo));
    }

}
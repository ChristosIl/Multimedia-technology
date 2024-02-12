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
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class HelloController {
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
    }
    @FXML
    protected void handleLoginAction() throws IOException {

        errorLabel.setText("");
        errorLabel_2.setText("");

        String usernameText = username.getText();
        String passwordText = password.getText();

        // Check if either the username or password fields are empty
        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            // Display message prompting the user to fill in all fields
            if (usernameText.isEmpty()) {
                errorLabel_2.setText("You must fill up this field");
            }
            if (passwordText.isEmpty()) {
                errorLabel.setText("You must fill up this field");
            }
        } else {
            List<User> users = UserDataManager.getInstance().getUsers(); // Adjusted to use getInstance().getUsers()
            boolean found = false;
            User loggedInUser = null;

            // Admin check (assuming admin has a special handling)
            if (usernameText.equalsIgnoreCase("admin") && passwordText.equals("admin")) {
                // Open admin dashboard
                loadDashboard("Dashboard.fxml");
                System.out.println("Login successfully!");
                System.out.println("Admin logged in");
                return;
            }

            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(usernameText) && user.getPassword().equals(passwordText)) {
                    found = true;
                    loggedInUser = user;
                    break;
                }
            }

            if (found && loggedInUser != null) {
                // Proceed to load user-specific dashboard
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

            // Optionally, create a new stage if you want the sign-up form to open in a new window
            // Stage stage = new Stage();

            // Set the sign-up scene to the stage and show it
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
        }
    }
    //user dashboard load
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

        // Sort the books by rating in descending order and limit to top 5
        List<Book> topRatedBooks = books.stream()
                .sorted(Comparator.comparingInt(Book::getRating).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Extract the titles (or any other information you want to display) of the top rated books
        List<String> topRatedBooksTitles = topRatedBooks.stream()
                .map(book -> book.getTitle() + " - Rating: " + book.getRating())
                .collect(Collectors.toList());

        // Display the titles in the ListView
        topRatedBooksListView.setItems(FXCollections.observableArrayList(topRatedBooksTitles));
    }
}
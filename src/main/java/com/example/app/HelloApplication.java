package com.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.app.UserDataManager;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize user list with admin if not already done
        UserDataManager.getInstance().initializeUserList();
        // Clear the user list (if needed)
        //UserDataManager.clearUserList();
        // Optionally, print users to console for debugging purposes
        UserDataManager.getInstance().printUsers();


        // Initialize BookManager and print list of books to console
        BookManager bookManager = BookManager.getInstance(); // Ensure this matches your Singleton pattern
        bookManager.printBooks(); // This will print the list of books to the console

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Library Management!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize user list with admin if not already done
        UserDataManager.getInstance().initializeUserList();
        // Clear the user list (if needed)
        //UserDataManager.clearUserList();

        // Optionally, print users to console for debugging purposes
        UserDataManager.getInstance().printUsers();

        // Initialize BookManager
        BookManager bookManager = BookManager.getInstance();
        // Clear comments from all books
        //  for (Book book : bookManager.getBooks()) {
        //     book.clearComments();
        // }
        // Save the books if your application uses persistent storage
        bookManager.saveBooks();

        BorrowingRecordManager borrowedbooks = BorrowingRecordManager.getInstance();
        borrowedbooks.printAllBorrowingRecords();

        // Print list of books to console
        bookManager.printBooks();


        // testCategorySerialization();

        //CategoryManager categoryManager = CategoryManager.getInstance();
        //categoryManager.clearCategories();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Library Management!");
        stage.setScene(scene);
        stage.show();
    }

    private void testcategoryserialization() {
        //singleton for CategoryMan
        CategoryManager categoryManager = CategoryManager.getInstance();


        categoryManager.addCategory("Test Category");
        categoryManager.saveCategories();
        System.out.println("Categories after adding and saving:");
        categoryManager.printCategories();

        //simulate app restart
        categoryManager.clearCategories();
        System.out.println("Categories after clearing:");
        categoryManager.printCategories(); // Should print nothing


        categoryManager.loadCategories();
        System.out.println("Categories after reloading:");
        categoryManager.printCategories(); //Should print "Test Category"
    }
    public static void main(String[] args) {
        launch();
    }
}



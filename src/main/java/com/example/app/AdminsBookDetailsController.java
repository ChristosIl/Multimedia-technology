package com.example.app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminsBookDetailsController {

    @FXML
    private Button GoBackButton;
    @FXML
    private Label titleLabel;
    //@FXML
    //private ListView<String> commentsListView;
    @FXML
    private Label ratingLabel;
    //private User currentUser;
    @FXML
    private VBox commentsVBox;
    private Book book;
    @FXML
    private void handlegobackAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) GoBackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    //initializing books
    public void setBook(Book book) {
        this.book = book;
        titleLabel.setText(book.getTitle());
        ratingLabel.setText("Average Rating: " + String.format("%.2f", book.getAverageRating()));

        commentsVBox.getChildren().clear();
        commentsVBox.setSpacing(10);

        //put each comment as a label dynamically
        for (String comment : book.getComments()) {
            Label commentLabel = new Label(comment);
            commentLabel.setWrapText(true);
            commentsVBox.getChildren().add(commentLabel);
        }
    }


}

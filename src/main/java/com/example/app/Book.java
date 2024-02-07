package com.example.app;
import java.io.Serializable;

public class Book implements Serializable{
    private static final long serialVersionUID = 1L; //needs for serialization
    //book's data
    private String title;
    private String author;
    private String publishingHouse;
    private String isbn;
    private int yearOfPublishing;
    private String category;
    private int numberOfCopies;

    //Constructor
    public Book(String title, String author, String publishingHouse, String isbn, int yearOfPublishing, String category, int numberOfCopies) {
        this.title = title;
        this.author = author;
        this.publishingHouse = publishingHouse;
        this.isbn = isbn;
        this.yearOfPublishing = yearOfPublishing;
        this.category = category;
        this.numberOfCopies = numberOfCopies;

    }

    //getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishingHouse() {
        return publishingHouse;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getYearOfPublishing() {
        return yearOfPublishing;
    }

    public String getCategory() {
        return category;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

}

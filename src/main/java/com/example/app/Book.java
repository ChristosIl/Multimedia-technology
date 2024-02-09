package com.example.app;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L; //needs for serialization

    //book's data
    private String title;
    private String author;
    private String publishingHouse;
    private String isbn;
    private int yearOfPublishing;
    private String category;
    private int numberOfCopies;

    // Constructor
    public Book(String title, String author, String publishingHouse, String isbn, int yearOfPublishing, String category, int numberOfCopies) {
        this.title = title;
        this.author = author;
        this.publishingHouse = publishingHouse;
        this.isbn = isbn;
        this.yearOfPublishing = yearOfPublishing;
        this.category = category;
        this.numberOfCopies = numberOfCopies;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setPublishingHouse(String publishingHouse) {
        this.publishingHouse = publishingHouse;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setYearOfPublishing(int yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    // Getters
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

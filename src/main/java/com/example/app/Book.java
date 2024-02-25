package com.example.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L; //needs for serialization

    private float averageRating;
    private int rating;
    //book's data
    private String title;
    private String author;
    private String publisher;
    private String publishingHouse;
    private String isbn;
    private int yearOfPublishing;
    private Category category;
    private int numberOfCopies;
    private List<Integer> ratings;
    private List<String> comments;
    // Constructor
    public Book(String title, String author, String publishingHouse, String isbn, int yearOfPublishing, Category category, int numberOfCopies) {
        this.title = title;
        this.author = author;
        this.publishingHouse = publishingHouse;
        this.isbn = isbn;
        this.yearOfPublishing = yearOfPublishing;
        this.category = category;
        this.numberOfCopies = numberOfCopies;
        this.ratings = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    // Setters
    public void setRating(int Rating) {
        this.rating = Rating;
    }
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
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    // Getters

    public int getRating() {
        return rating;
    }
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

    public Category getCategory() {
        return category;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        double total = ratings.stream().mapToInt(Integer::intValue).sum();
        return total / ratings.size();
    }

    public void addRating(int rating) {
        if (ratings == null) {
            ratings = new ArrayList<>(); //Initialize if null
        }
        ratings.add(rating);
    }

    public void addComment(String comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

    public List<String> getComments() {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        return this.comments;
    }

    public void clearComments() {
        if (this.comments != null) {
            this.comments.clear();
        }
    }

    public int howManyRatings(){
        if(ratings == null || ratings.isEmpty()){
            return 0;
        } else return ratings.size();
    }
}

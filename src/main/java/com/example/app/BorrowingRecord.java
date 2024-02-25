package com.example.app;
import java.io.Serializable;
import java.time.LocalDate;

public class BorrowingRecord implements Serializable{
    private static final long serialVersionUID = 4L;
    private String userIdNumber;
    private String bookIsbn;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String title;
    private String author;

    //constructor
    public BorrowingRecord(String userIdNumber, String bookIsbn, LocalDate borrowDate, String title, String author) {
        this.userIdNumber = userIdNumber;
        this.bookIsbn = bookIsbn;
        this.borrowDate = borrowDate;
        this.returnDate = borrowDate.plusDays(5); //borrowing period + 5 days
        this.title = title;
        this.author = author;
    }
    //getters
    public String getUserIdNumber() {
        return userIdNumber;
    }
    public String getBookIsbn() {
        return bookIsbn;
    }
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }

}

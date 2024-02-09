package com.example.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookManager {
    private static BookManager instance; //Singleton instance
    private List<Book> books;
    private static final String BOOKS_FILE = "books.ser";

    public BookManager() {
        books = loadBooks();
    }

    //Public method to get the singleton instance
    public static synchronized BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
    }

    private void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Book> loadBooks() {
        File file = new File(BOOKS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<Book>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    //for debugging
    public void printBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            System.out.println("List of books:");
            for (Book book : books) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor() +
                        ", ISBN: " + book.getIsbn() + ", Year: " + book.getYearOfPublishing() +
                        ", Category: " + book.getCategory() + ", Copies: " + book.getNumberOfCopies());
            }
        }
    }

    //deleting a book
    public void deleteBook(Book book) {
        books.removeIf(b -> b.getIsbn().equals(book.getIsbn()));
        saveBooks(); // Ensure this method writes the updated list to the file
    }

    //TODO
    public void deleteBooksByCategory(String category) {
        books.removeIf(book -> book.getCategory().equals(category));
        saveBooks();
    }

    public void decreaseBookCopies(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn) && book.getNumberOfCopies() > 0) {
                book.setNumberOfCopies(book.getNumberOfCopies() - 1);
                saveBooks();
                break;
            }
        }
    }




}

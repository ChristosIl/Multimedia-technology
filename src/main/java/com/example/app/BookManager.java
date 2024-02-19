package com.example.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookManager {
    private static BookManager instance; //Singleton instance
    private List<Book> books;
    private static final String BOOKS_FILE = "books.ser";

    public BookManager() {
        books = loadBooks();
    }

    public List<Book> getTopRatedBooks(int limit) {
        return books.stream()
                .filter(book -> book.getAverageRating() > 0) // Ensure the book has been rated
                .sorted((b1, b2) -> Double.compare(b2.getAverageRating(), b1.getAverageRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    //Public method to get the singleton instance
    public static synchronized BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    public void addBook(Book book) {
        Category category = CategoryManager.getInstance().findOrCreateCategory(book.getCategory().getName());
        book.setCategory(category);
        books.add(book);
        saveBooks();
    }

    public void saveBooks() {
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
        BorrowingRecordManager.getInstance().deleteRecordsByBookIsbn(book.getIsbn());
    }

    //TODO
    public void deleteBooksByCategory(String categoryName) {
        System.out.println("Finding books in category: " + categoryName);
        List<Book> booksToRemove = books.stream()
                .filter(book -> book.getCategory().getName().equals(categoryName))
                .collect(Collectors.toList());
        System.out.println("Books to remove: " + booksToRemove.size());
        // Iterate over the list of books to remove
        for (Book book : booksToRemove) {
            System.out.println("Deleting book: " + book.getIsbn());
            deleteBook(book);
        }

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

    public void increaseBookCopies(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                book.setNumberOfCopies(book.getNumberOfCopies() + 1);
                saveBooks();
                break;
            }
        }
    }

    public Book getBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null; // Book not found
    }

    public void updateBookCategory(String oldCategory, String newCategory) {
        for (Book book : books) {
            if (book.getCategory().getName().equalsIgnoreCase(oldCategory)) {
                book.setCategory(new Category(newCategory)); // Assuming Category reference in Book
            }
        }
        saveBooks();
    }

    public int getBookCountByCategory(String categoryName) {
        int count = 0;
        for (Book book : books) {
            if (book.getCategory() != null && book.getCategory().getName().equalsIgnoreCase(categoryName)) {
                count++;
            }
        }
        return count;
    }

}

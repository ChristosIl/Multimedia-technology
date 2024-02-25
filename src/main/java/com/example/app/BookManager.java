package com.example.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages a collection of books, including operations to add, delete, and update books.
 * This class employs the Singleton design pattern to ensure only one instance manages the book collection.
 */
public class BookManager {
    private static BookManager instance; //Singleton instance
    private List<Book> books; //Collection of Books

    //Path to the serialized file storing the books
    private static final String BOOKS_FILE = "medialab" + File.separator + "books.ser";

    public BookManager() {
        books = loadBooks();
    }


    /**
     * Provides the singleton instance of the BookManager class.
     * If the instance does not exist, it creates one. Otherwise, it returns the existing instance.
     *
     * @return The singleton instance of BookManager.
     */
    public static synchronized BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    /**
     * Adds a book to the collection and saves the updated collection to a file
     * that is specified in savebooks().
     *
     * @param book The book to be added.
     */
    public void addBook(Book book) {
        Category category = CategoryManager.getInstance().findOrCreateCategory(book.getCategory().getName());
        book.setCategory(category);
        books.add(book);
        saveBooks();
    }
    /**
     * Saves the current list of books to a file we defined at line 17, in the string BOOKS_FILE.
     */
    public void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads the list of books from the file we defined at line 17.
     *
     * @return A list of books loaded from the file.
     */
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

    /**
     * Returns the list of all books.
     *
     * @return A list containing all books.
     */
    public List<Book> getBooks() {
        return books;
    }



    /**
     * Prints a list of all books in the console for debugging purposes.
     * Each book's title, author, ISBN, year of publishing, category, and number of copies are displayed.
     */
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

    /**
     * Deletes a book from the collection (and from the borrowed books collection)
     * based on its ISBN and updates the file.
     *
     * @param book The book to be deleted.
     */
    public void deleteBook(Book book) {
        books.removeIf(b -> b.getIsbn().equals(book.getIsbn()));
        saveBooks();
        BorrowingRecordManager.getInstance().deleteRecordsByBookIsbn(book.getIsbn());
    }

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

    /**
     * Decreases the number of copies of a book by one.
     *
     * @param isbn The ISBN of the book whose copies are going to be decreased.
     */
    public void decreaseBookCopies(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn) && book.getNumberOfCopies() > 0) {
                book.setNumberOfCopies(book.getNumberOfCopies() - 1);
                saveBooks();
                break;
            }
        }
    }
    /**
     * Increases the number of copies of a book by one.
     *
     * @param isbn The ISBN of the book whose copies are going to be increased.
     */
    public void increaseBookCopies(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                book.setNumberOfCopies(book.getNumberOfCopies() + 1);
                saveBooks();
                break;
            }
        }
    }

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn The ISBN of the book to be retrieved.
     * @return The book with the specified ISBN, or null if not found.
     */
    public Book getBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        //Book not found
        return null;
    }

    /**
     * Updates the category of a book with a new one.
     *
     * @param oldCategory The current category name of the book.
     * @param newCategory The new category name to be set.
     */
    public void updateBookCategory(String oldCategory, String newCategory) {
        for (Book book : books) {
            if (book.getCategory().getName().equalsIgnoreCase(oldCategory)) {
                book.setCategory(new Category(newCategory));
            }
        }
        saveBooks();
    }

    /**
     * Gets the count of books in a specific category.
     *
     * @param categoryName The name of the category.
     * @return The number of books in the specified category.
     */
    public int getBookCountByCategory(String categoryName) {
        int count = 0;
        for (Book book : books) {
            if (book.getCategory() != null && book.getCategory().getName().equalsIgnoreCase(categoryName)) {
                count++;
            }
        }
        return count;
    }


    /**
     * Updates the details of a book in the collection.
     *
     * @param updatedBook The book with updated details.
     */
    public void updateBook(Book updatedBook) {
        //trying to find the book by its isbn
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getIsbn().equals(updatedBook.getIsbn())) {
                //if found we update the book's details
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setYearOfPublishing(updatedBook.getYearOfPublishing());
                book.setNumberOfCopies(updatedBook.getNumberOfCopies());
                book.setCategory(updatedBook.getCategory());
                saveBooks();
                return;
            }
        }
    }

    /**
     * Checks if an ISBN exists in the collection, excluding a specific ISBN from the check.
     *
     * @param isbn The ISBN to check.
     * @param excludeIsbn The ISBN to exclude from the check(in our usage, the ISBN of the current book,
     * so that it does not affect the EditButton if we will not make any change on it(book)).
     * @return true if the ISBN exists in the collection excluding the specified ISBN; false if it does not exists.
     */
    public boolean isbnExists(String isbn, String excludeIsbn) {
        return books.stream()
                .anyMatch(book -> book.getIsbn().equals(isbn) && !book.getIsbn().equals(excludeIsbn));
    }

}

package com.example.app;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class BorrowingRecordManager {

    private static BorrowingRecordManager instance; //Singleton instance
    private List<BorrowingRecord> borrowingRecords; //list of borrowing books
    private static final String BORROWED_BOOKS_FILE = "borrowedbooks.ser";

    public BorrowingRecordManager() { borrowingRecords = loadBorrowingRecords(); }

    public static synchronized BorrowingRecordManager getInstance() {
        if (instance == null) {
            instance = new BorrowingRecordManager();
        }
        return instance;
    }

    public void addRecord(BorrowingRecord record) {
        borrowingRecords.add(record);
        saveBorrowingRecords();
    }

    @SuppressWarnings("unchecked")
    private List<BorrowingRecord> loadBorrowingRecords() {
        File file = new File(BORROWED_BOOKS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<BorrowingRecord>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private void saveBorrowingRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BORROWED_BOOKS_FILE))) {
            oos.writeObject(borrowingRecords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<BorrowingRecord> getBorrowingRecords() {
        return borrowingRecords;
    }

    //use it on admin

    public List<BorrowingRecord> getRecordsByUserId(String userId) {
        List<BorrowingRecord> userRecords = new ArrayList<>();
        for (BorrowingRecord record : borrowingRecords) {
            if (record.getUserIdNumber().equals(userId)) {
                userRecords.add(record);
            }
        }
        return userRecords;
    }

    public void printAllBorrowingRecords() {
        if (borrowingRecords.isEmpty()) {
            System.out.println("No books have been borrowed.");
        } else {
            System.out.println("List of Borrowed Books:");
            for (BorrowingRecord record : borrowingRecords) {
                System.out.printf("User ID: %s, Book ISBN: %s, Borrow Date: %s, Return Date: %s%n",
                        record.getUserIdNumber(), record.getBookIsbn(),
                        record.getBorrowDate().toString(), record.getReturnDate().toString());
            }
        }
    }

    public int countActiveBorrowingsForUser(String userIdNumber) {
        return (int) borrowingRecords.stream()
                .filter(record -> record.getUserIdNumber().equals(userIdNumber) && !record.getReturnDate().isBefore(LocalDate.now()))
                .count();
    }

    public void returnBook(String userId, String isbn) {
        BorrowingRecord recordToReturn = null;
        for (BorrowingRecord record : borrowingRecords) {
            if (record.getUserIdNumber().equals(userId) && record.getBookIsbn().equals(isbn)) {
                recordToReturn = record;
                break;
            }
        }

        if (recordToReturn != null) {
            //Increase book copies in BookManager
            BookManager.getInstance().increaseBookCopies(isbn);
            // Remove the record or mark it as returned based on your design
            borrowingRecords.remove(recordToReturn);
            saveBorrowingRecords(); // Don't forget to persist the changes
        } else {
            System.out.println("No borrowing record found for the book to return.");
        }
    }

    public void removeRecord(String userId, String isbn) {
        // Find the record to remove
        BorrowingRecord recordToRemove = borrowingRecords.stream()
                .filter(record -> record.getUserIdNumber().equals(userId) && record.getBookIsbn().equals(isbn))
                .findFirst()
                .orElse(null); // or use .findAny() if you're not concerned about finding multiple matches

        // If a record was found, remove it and update the book's availability
        if (recordToRemove != null) {
            borrowingRecords.remove(recordToRemove);
            saveBorrowingRecords();
            // Optionally, increase book copies in BookManager if you want to reflect the return in the inventory
            BookManager.getInstance().increaseBookCopies(isbn);
        } else {
            System.out.println("No borrowing record found for the given user ID and ISBN.");
        }
    }
    public void deleteUserLoans(String userId) {
        List<BorrowingRecord> recordsToDelete = borrowingRecords.stream()
                .filter(record -> record.getUserIdNumber().equals(userId))
                .collect(Collectors.toList());

        for (BorrowingRecord record : recordsToDelete) {
            BookManager.getInstance().increaseBookCopies(record.getBookIsbn());
        }

        borrowingRecords.removeIf(record -> record.getUserIdNumber().equals(userId));
        saveBorrowingRecords();
    }
}



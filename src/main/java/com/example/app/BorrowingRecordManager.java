package com.example.app;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
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

}

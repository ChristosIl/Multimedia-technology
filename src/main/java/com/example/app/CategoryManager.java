package com.example.app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    private List<Category> categories;
    private static CategoryManager instance;
    private static final String CATEGORIES_FILE = "categories.ser";

    private CategoryManager() {this.categories = loadCategories();}

    public static synchronized CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    public boolean addCategory(String name) {
        if (categories.stream().noneMatch(category -> category.getName().equalsIgnoreCase(name))) {
            categories.add(new Category(name));
            saveCategories();
            return true; // Indicate success
        }
        return false; // Category already exists
    }

    public void deleteCategory(String name) {
        System.out.println("Attempting to delete category: " + name);
        if (categories.removeIf(category -> category.getName().equals(name))) {
            saveCategories(); // Save after deleting
            System.out.println("Category deleted: " + name);
        }
        BookManager.getInstance().deleteBooksByCategory(name);
        System.out.println("Attempting to delete books in category: " + name);
    }

    public void editCategory(String oldName, String newName) {
        boolean found = false;
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(oldName)) {
                category.setName(newName);
                found = true;
                break;
            }
        }
        if (found) {
            // Update all books that belonged to the old category to the new category name
            BookManager.getInstance().updateBookCategory(oldName, newName);
            saveCategories();
        }
        // Optionally, save changes to persistent storage
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void printCategories() {
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
        } else {
            System.out.println("List of Categories:");
            for (Category category : categories) {
                System.out.println(category.getName());
            }
        }
    }

    public void saveCategories() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CATEGORIES_FILE))) {
            oos.writeObject(categories); // Serialize the entire list
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Category> loadCategories() {
        File file = new File(CATEGORIES_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<Category>) ois.readObject(); // Deserialize the entire list
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(); // Return an empty list if the file doesn't exist or an error occurs
    }
    public Category findOrCreateCategory(String name) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        // If not found, create a new category and add it to the list
        Category newCategory = new Category(name);
        categories.add(newCategory);
        return newCategory;
    }

    public void clearCategories() {
        this.categories.clear();
    }
}


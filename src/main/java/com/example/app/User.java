package com.example.app;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String name;
    private String surname;
    private String idNumber;
    private String email;

    //Constructor for regular users
    public User(String username, String password, String name, String surname, String idNumber, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.email = email;
    }

    //Constructor for admin user (it is not needed, but we implemented for future updates maybe)
    public User(String username, String password) {
        this(username, password, "", "", "", "");
    }

    //Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getEmail() {
        return email;
    }

    //Setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}


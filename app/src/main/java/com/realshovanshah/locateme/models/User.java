package com.realshovanshah.locateme.models;

public class User {
    String userId, username, email, fname, phone;

    public User() {
    }

    public User(String userId, String username, String email, String fname, String phone) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fname = fname;
        this.phone = phone;
    }

    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFname() {
        return fname;
    }

    public String getPhone() {
        return phone;
    }
}

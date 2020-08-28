package com.realshovanshah.locateme.models;

public class Users {
    String username, email, fname, phone, password;

    public Users() {
    }

    public Users(String username, String email, String fname, String phone, String password) {
        this.username = username;
        this.email = email;
        this.fname = fname;
        this.phone = phone;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}

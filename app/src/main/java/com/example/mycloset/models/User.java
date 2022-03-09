package com.example.mycloset.models;
/**
 * This class represents a My closet user
 */
public class User {

    private String fullName;
    private String email;
    private String pw;


    public User(String fullName,String email,String pw) {
        this.fullName = fullName;
        this.email = email;
        this.pw = pw;
    }
    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPw() {
        return pw;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}

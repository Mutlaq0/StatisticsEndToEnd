package com.example.myapplication;

import java.util.List;

public class User {

    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private List<String> kids; // contains references to kids(id numbers of kid)
    public User() {
    }
    public User(String fullName, String phoneNumber, String email, String password) {
        super();
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
    public User(String fullName, String phoneNumber, String email) {
        super();
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<String> getKids() {
        return kids;
    }
    public void setKids(List<String> kids) {
        this.kids = kids;
    }
    @Override
    public String toString() {
        return "User [fullName=" + fullName + ", phoneNumber=" + phoneNumber + ", email=" + email + ", password="
                + password + ", kids=" + kids + "]";
    }

}


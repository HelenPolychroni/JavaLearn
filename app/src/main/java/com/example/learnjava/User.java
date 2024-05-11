package com.example.learnjava;


public class User {

    protected String email,gender;

    User(){}
    public User(String email, String gender) {
        this.email = email;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

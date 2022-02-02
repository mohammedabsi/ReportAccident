package com.example.myapplication;

public class User {
    public String  email, password, userId;


    public User() {

    }

    public User( String userId, String password, String email) {

        this.userId = userId;
        this.password = password;
        this.email = email;



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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}

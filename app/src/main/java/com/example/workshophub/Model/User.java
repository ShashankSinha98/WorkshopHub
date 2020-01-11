package com.example.workshophub.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String username;
    private String password;
    private String email;
    private String workshopRegistered;

    public String getWorkshopRegistered() {
        return workshopRegistered;
    }

    public void setWorkshopRegistered(String workshopRegistered) {
        this.workshopRegistered = workshopRegistered;
    }



    public User(){

    }

    public User(String id, String username, String password, String email, String workshopRegistered) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.workshopRegistered = workshopRegistered;
    }

    public String getId() {
        return id;
    }

    public User(String id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

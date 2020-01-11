package com.example.workshophub.Model;

public class Workshop {

    private String id;
    private String name;
    private int image;
    private String description;
    private String location;
    private String date;
    private String time;
    private String userStatus;
    private String venue;

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Workshop(){

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Workshop(String id, String name, int image, String description, String location, String date, String time, String userStatus, String venue) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.userStatus = userStatus;
        this.venue = venue;
    }

    public Workshop(String name, int image, String description, String location, String date, String time, String userStatus) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.userStatus = userStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}

package com.capstoneproject.pedromarco.eventapp.entities;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Model of an Event.
 * Contains all parameters needed of an Event, as well as all getters and setters.
 */
public class Event implements Serializable {

    private String id;
    private String date;
    private String time;
    private String name;
    private String description;
    private float rating;
    private String category;
    private String location;
    private double latitude;
    private double longitude;
    private String photoURL;
    private String creator;
    private HashMap<String, Comment> comments;
    private int maxPeople;
    private int currentPeople;

    public Event() {
    }

    public Event(String date, String time, String name, String description, float rating, String category, String location, double latitude, double longitude, String photoURL, int maxPeople, int currentPeople, String creator, HashMap<String, Comment> comments) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.category = category;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoURL = photoURL;
        this.maxPeople = maxPeople;
        this.currentPeople = currentPeople;
        this.creator = creator;
        this.comments = comments;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getCurrentPeople() {
        return currentPeople;
    }

    public void setCurrentPeople(int currentPeople) {
        this.currentPeople = currentPeople;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }


    public HashMap<String, Comment> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, Comment> comments) {
        this.comments = comments;
    }

}


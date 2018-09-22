package com.capstoneproject.pedromarco.eventapp.entities;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Model of an User.
 * Contains all parameters needed of an User, as well as all getters and setters.
 */
public class User implements Serializable {

    private String id;
    private String username;
    private String firstname;
    private String surname;
    private String description;
    private float avgRating;
    private Integer numEventsAssisted;
    private Integer numEventCreated;
    private Integer numOfRatings;
    private String profilePhotoURL;
    private HashMap<String, String> favourite_events;
    private HashMap<String, String> assisting_events;
    private HashMap<String, String> rated_events;

    public User() {
    }

    public User(String id, String username, String name, String surname, String description, String profilePhotoURL, float avgRating, Integer numOfRatings, Integer numEventsAssisted, Integer numEventCreated, HashMap<String, String> favourite_events, HashMap<String, String> assisting_events, HashMap<String, String> rated_events) {
        this.id = id;
        this.username = username;
        this.firstname = name;
        this.surname = surname;
        this.description = description;
        this.avgRating = avgRating;
        this.numOfRatings = numOfRatings;
        this.numEventsAssisted = numEventsAssisted;
        this.profilePhotoURL = profilePhotoURL;
        this.numEventCreated = numEventCreated;
        this.favourite_events = favourite_events;
        this.assisting_events = assisting_events;
        this.rated_events = rated_events;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getNumEventsAssisted() {
        return numEventsAssisted;
    }

    public void setNumEventsAssisted(Integer numEventsAssisted) {
        this.numEventsAssisted = numEventsAssisted;
    }

    public Integer getNumEventCreated() {
        return numEventCreated;
    }

    public void setNumEventCreated(Integer numEventCreated) {
        this.numEventCreated = numEventCreated;
    }

    public String getProfilePhotoURL() {
        return profilePhotoURL;
    }

    public void setProfilePhotoURL(String profilePhotoURL) {
        this.profilePhotoURL = profilePhotoURL;
    }

    public HashMap<String, String> getFavourite_events() {
        return favourite_events;
    }

    public void setFavourite_events(HashMap<String, String> favourite_events) {
        this.favourite_events = favourite_events;
    }

    public HashMap<String, String> getAssisting_events() {
        return assisting_events;
    }

    public void setAssisting_events(HashMap<String, String> assisting_events) {
        this.assisting_events = assisting_events;
    }

    public Integer getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(Integer numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public HashMap<String, String> getRated_events() {
        return rated_events;
    }

    public void setRated_events(HashMap<String, String> rated_events) {
        this.rated_events = rated_events;
    }
}

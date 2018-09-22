package com.capstoneproject.pedromarco.eventapp.entities;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Model of the Comment.
 * Contains the username of the writer, the comment text, the pictureURL of the user, and the time in miliseconds, as well as all getters and setters.
 */
public class Comment implements Comparable<Comment>, Serializable {
    private String username;
    private String comment;
    private String pictureURL;
    private String time; //stores the time of the comment in miliseconds

    public Comment(String username, String comment, String pictureURL, String time) {
        this.username = username;
        this.comment = comment;
        this.pictureURL = pictureURL;
        this.time = time;
    }

    public Comment() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(@NonNull Comment comment) {
        return time.compareTo(comment.time);
    }
}

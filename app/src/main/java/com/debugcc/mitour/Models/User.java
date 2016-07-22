package com.debugcc.mitour.Models;

import android.util.Log;

/**
 * Created by dubgcc on 06/06/16.
 */
public class User {
    public static String FACEBOOK_SERVER = "facebook";
    public static String GOOGLE_SERVER = "google";

    public String userID;
    public String name;
    public String email;
    public String urlProfilePicture;
    public String server;

    public User() {
        this.userID = "";
        this.name = "";
        this.email = "";
        this.urlProfilePicture = "";
        this.server = "";
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlProfilePicture() {
        return urlProfilePicture;
    }

    public void setUrlProfilePicture(String urlProfilePicture) {
        this.urlProfilePicture = urlProfilePicture;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void print() {
        Log.e("USER", "ID: "+userID+", name: "+name+", email: "+email+", url: "+urlProfilePicture );
    }

    public static User CURRENT_USER = null;
}

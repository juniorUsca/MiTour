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

    public void print() {
        Log.e("USER", "ID: "+userID+", name: "+name+", email: "+email+", url: "+urlProfilePicture );
    }
}

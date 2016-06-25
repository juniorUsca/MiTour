package com.debugcc.mitour.Models;

import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dubgcc on 24/06/16.
 */
@IgnoreExtraProperties
public class City {
    private String ID;
    private String city;
    private String country;
    //String URL;
    //String


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Log.d("CITY", "toMap: ");
        HashMap<String, Object> result = new HashMap<>();
        result.put("city", city);
        result.put("country", country);
        result.put("modifiedAt", ServerValue.TIMESTAMP);
        return result;
    }
}

package com.debugcc.mitour.Models;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dubgcc on 24/06/16.
 */
@IgnoreExtraProperties
public class City {
    private String ID;
    private String city;
    private String country;
    private String details;
    private Boolean popular;
    private String image_url;
    private Bitmap image;

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String image_url) {
        this.image_url = image_url;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Log.d("CITY", "toMap: ");
        HashMap<String, Object> result = new HashMap<>();
        result.put("city", city);
        result.put("country", country);
        result.put("image_url", image_url);
        result.put("modifiedAt", ServerValue.TIMESTAMP);
        return result;
    }

    @Exclude
    public static City CURRENT_CITY = null;

    @Exclude
    public static final List<City> CITIES = new ArrayList<>();

    @Exclude
    public static final Map<String, City> CITIES_MAP = new HashMap<>();

    @Exclude
    public static final List<City> POPULAR_CITIES = new ArrayList<>();

    @Exclude
    public static final Map<String, City> POPULAR_CITIES_MAP = new HashMap<>();

}

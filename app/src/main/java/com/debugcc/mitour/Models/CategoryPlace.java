package com.debugcc.mitour.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dubgcc on 28/05/16.
 */
public class CategoryPlace {
    private int image;
    private String name;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<CategoryPlace> parse(JSONArray json) {
        ArrayList<CategoryPlace> temp = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            CategoryPlace c = new CategoryPlace();
            try {
                JSONObject jobject = (JSONObject) json.get(i);
                c.setName(jobject.getString("name"));
                //c.setImage(jobject.getString("image"));
                temp.add(c);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return temp;
    }
}

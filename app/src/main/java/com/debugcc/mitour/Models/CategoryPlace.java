package com.debugcc.mitour.Models;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dubgcc on 28/05/16.
 */
@IgnoreExtraProperties
public class CategoryPlace {
    private String ID;
    private Bitmap image;
    private String name;
    private String url;
    private Long replace;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Exclude
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

    @Exclude
    public Map<String, Object> toMap() {
        Log.d("TOMAP", "toMap: ");
        HashMap<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("name", name);
        result.put("replace", ServerValue.TIMESTAMP);
        return result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getReplace() {
        return replace;
    }

    public void setReplace(Long replace) {
        this.replace = replace;
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

    @Exclude
    public static int findByID(ArrayList<CategoryPlace> cps, String ID) {
        for (int i = 0; i < cps.size(); i++)
            if ( cps.get(i).getID().equals(ID) )
                return i;
        return -1;
    }
}

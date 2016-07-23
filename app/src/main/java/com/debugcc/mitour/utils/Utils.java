package com.debugcc.mitour.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.debugcc.mitour.Models.User;
import com.debugcc.mitour.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dubgcc on 05/06/16.
 */
public class Utils {
    private static final String TAG = "Utils";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    private static final String PREFERENCES_FILE = "MiTour_settings";

    public static final String PREF_FIRST_SYNC = "Pref_First_Sync";
    public static final String PREF_SPLASH_COMPLETED = "Pref_Splash_Completed";
    public static final String PREF_USER_FIRST_TIME = "Pref_User_First_Time";

    public static final String FIRE_DB_CATEGORIES = "categories";
    public static final String FIRE_DB_MARKERS = "markers";





    public static int getToolbarHeight(Context context) {
        int height = (int) context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        return height;
    }

    public static int getStatusBarHeight(Context context) {
        int height = (int) context.getResources().getDimension(R.dimen.statusbar_size);
        return height;
    }


    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }


    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }


    public static <T> T readSharedList(Context ctx, String settingName, Class<T> cls) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        String jsonItems = sharedPref.getString(settingName, null);
        Gson gson = new Gson();
        return gson.fromJson(jsonItems, cls);
    }

    public static void saveSharedList(Context ctx, String settingName, ArrayList items) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        editor.putString(settingName, gson.toJson(items.toArray()));
        editor.apply();
    }


    /// PROFILE

    public static Bitmap getProfilePicture(String url){
        Bitmap bitmap = null;
        try {
            URL imageURL = new URL(url);
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * Function to download images on ASYNC
     *
     * Long replace is a timestamp to know if we need replace the image
     */
    public static void putPicture(Context ctx, String name, String url, Long replace){
        Bitmap bitmap = null;

        String path = ctx.getFilesDir().getAbsolutePath() + "/" + name;
        File file = new File (path);
        //if (!file.exists()) {
        if (replace > file.lastModified()) {
            try {
                Log.d(TAG, "DOWNLOADING Picture: " + name + " " + url);
                URL imageURL = new URL(url);
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                outputStream.close();

                //Log.d(TAG, "putPicture: NEW LAST MODIFIED " + file.lastModified() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getPicture(Context ctx, String name, Drawable drw_default){
        Bitmap bitmap = null;

        String path = ctx.getFilesDir().getAbsolutePath() + "/" + name;
        File file = new File (path);
        if (!file.exists()) {
            //bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_home_vector);
            bitmap = ((BitmapDrawable) drw_default).getBitmap();
        } else {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return bitmap;
        // (Drawable) return new BitmapDrawable(bitmap);
    }

    public static Bitmap getPicture(Context ctx, String name, Bitmap bm_default){
        Bitmap bitmap = null;

        String path = ctx.getFilesDir().getAbsolutePath() + "/" + name;
        File file = new File (path);
        if (!file.exists()) {
            bitmap = bm_default;
        } else {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return bitmap;
        // (Drawable) return new BitmapDrawable(bitmap);
    }


    /// MOBILE MODEL

    /** Returns the consumer friendly device name */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

//        String phrase = "";
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
//                phrase += Character.toUpperCase(c);
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
//            phrase += c;
            phrase.append(c);
        }

        return phrase.toString();
    }


    /// DIALOGS

    public static void confirmFeedBack(Context ctx) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("hola")
                .setTitle("mi titulo");

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /// CHARGE DATA

    public static void chargeDataLogged() {
        AsynchronousTasks.getCitiesForHome();
    }
}

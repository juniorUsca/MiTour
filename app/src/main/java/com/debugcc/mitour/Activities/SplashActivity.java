package com.debugcc.mitour.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.debugcc.mitour.Models.CategoryPlace;
import com.debugcc.mitour.Models.City;
import com.debugcc.mitour.Models.Marker;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.AsynchronousTasks;
import com.debugcc.mitour.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //new PrefetchData().execute();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/


        Utils.saveSharedSetting(this, Utils.PREF_FIRST_SYNC, Utils.TRUE);

        mDatabase = AsynchronousTasks.getDatabase();

            /*String key = mDatabase.child("categories").push().getKey();

            CategoryPlace p = new CategoryPlace();
            p.setUrl("http://icons.iconarchive.com/icons/icons8/windows-8/128/Travel-Campfire-icon.png");
            p.setName("fuego");

            Map<String, Object> postValues = p.toMap();
            Log.d(TAG, "doInBackground: changing values");

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/categories/" + key, postValues);
            mDatabase.updateChildren(childUpdates);*/

        // INSERTING MARKERS TEST
        /*String key = mDatabase.child("markers/-KL2gZ3gFpi_H6tiPsM0").push().getKey();
        Marker m1 = new Marker();
        m1.setName("Monasterio de Santa Catalina");
        m1.setDetails("lugar turistico");
        m1.setLat("-16.395331");
        m1.setLng("-71.536791");
        m1.setCategories(Arrays.asList("-KKeWtccXLIvm9HrILMD"));
        Map<String, Object> postValues = m1.toMap();
        Log.d(TAG, "onCreate: CREATING MARKER");
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/markers/-KL2gZ3gFpi_H6tiPsM0/" + key, postValues);
        mDatabase.updateChildren(childUpdates);

        key = mDatabase.child("markers/-KL2gZ3gFpi_H6tiPsM0").push().getKey();
        Marker m2 = new Marker();
        m2.setName("Claustros de la compañia");
        m2.setDetails("lugar turistico");
        m2.setLat("-16.399825");
        m2.setLng("-71.536548");
        m2.setCategories(Arrays.asList("-KKeWtccXLIvm9HrILMD"));
        postValues = m2.toMap();
        Log.d(TAG, "onCreate: CREATING MARKER");
        Map<String, Object> childUpdates2 = new HashMap<>();
        childUpdates2.put("/markers/-KL2gZ3gFpi_H6tiPsM0/" + key, postValues);
        mDatabase.updateChildren(childUpdates2);

        key = mDatabase.child("markers/-KL2gZ3gFpi_H6tiPsM0").push().getKey();
        Marker m3 = new Marker();
        m3.setName("Mirador de Yanahuara");
        m3.setDetails("lugar turistico");
        m3.setLat("-16.387506");
        m3.setLng("-71.541742");
        m3.setCategories(Arrays.asList("-KKeWtccXLIvm9HrILMD"));
        postValues = m3.toMap();
        Log.d(TAG, "onCreate: CREATING MARKER");
        Map<String, Object> childUpdates3 = new HashMap<>();
        childUpdates3.put("/markers/-KL2gZ3gFpi_H6tiPsM0/" + key, postValues);
        mDatabase.updateChildren(childUpdates3);

        key = mDatabase.child("markers/-KL2gZ3gFpi_H6tiPsM0").push().getKey();
        Marker m4 = new Marker();
        m4.setName("Mirador de Carmen Alto");
        m4.setDetails("lugar turistico");
        m4.setLat("-16.369861");
        m4.setLng("-71.536464");
        m4.setCategories(Arrays.asList("-KKeWtccXLIvm9HrILMD"));
        postValues = m4.toMap();
        Log.d(TAG, "onCreate: CREATING MARKER");
        Map<String, Object> childUpdates4 = new HashMap<>();
        childUpdates4.put("/markers/-KL2gZ3gFpi_H6tiPsM0/" + key, postValues);
        mDatabase.updateChildren(childUpdates4);

        key = mDatabase.child("markers/-KL2gZ3gFpi_H6tiPsM0").push().getKey();
        Marker m5 = new Marker();
        m5.setName("Mirador de Carmen Alto");
        m5.setDetails("lugar turistico");
        m5.setLat("-16.395491");
        m5.setLng("-71.534335");
        m5.setCategories(Arrays.asList("-KKeWtccXLIvm9HrILMD"));
        postValues = m5.toMap();
        Log.d(TAG, "onCreate: CREATING MARKER");
        Map<String, Object> childUpdates5 = new HashMap<>();
        childUpdates5.put("/markers/-KL2gZ3gFpi_H6tiPsM0/" + key, postValues);
        mDatabase.updateChildren(childUpdates5);*/

        /*String key = mDatabase.child("cities").push().getKey();

        City c = new City();
        c.setCity("Arequipa");
        c.setCountry("Peru");

        Map<String, Object> postValues = c.toMap();
        Log.d(TAG, "onCreate: CREATING CITY");

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/cities/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
        Log.d(TAG, "CITY CREATED");*/


        DatabaseReference categoriesRef = mDatabase.child(Utils.FIRE_DB_CATEGORIES);
        categoriesRef.keepSynced(true);

        categoriesRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: OBTENIENDO CATEGORIAS" );
                new PrefetchData().execute(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: ", databaseError.toException());
            }
        });

        DatabaseReference markersRef = mDatabase.child(Utils.FIRE_DB_MARKERS + "/-KL2gZ3gFpi_H6tiPsM0");
        markersRef.keepSynced(true);

        markersRef.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: OBTENIENDO MARCADORES" );
                //new PrefetchData().execute(dataSnapshot);
                if (dataSnapshot.hasChildren()) {

                    Iterable<DataSnapshot> ds_markers = dataSnapshot.getChildren();
                    ArrayList<Marker> markers = new ArrayList<>();

                    for (DataSnapshot ds : ds_markers) {

                        Marker m = new Marker();
                        m.setID(ds.getKey());
                        m.setName(ds.child("name").getValue(String.class));
                        m.setDetails(ds.child("details").getValue(String.class));
                        m.setLat(ds.child("lat").getValue(String.class));
                        m.setLng(ds.child("lng").getValue(String.class));
                        GenericTypeIndicator<List<String>> t_list = new GenericTypeIndicator<List<String>>() {};
                        m.setCategories(ds.child("categories").getValue(t_list));
                        //ServerValue.TIMESTAMP
                        //Utils.putPicture(getBaseContext(), m.getName(), m.getUrl(), m.getReplace());
                        markers.add(m);
                        Log.d(TAG, "onDataChange: MARKER" + m.toMap().toString());
                    }

                    Utils.saveSharedList(getBaseContext(), Utils.FIRE_DB_MARKERS, markers);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled: ", databaseError.toException());
            }
        });
    }

    private class PrefetchData extends AsyncTask<DataSnapshot, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /// before making http calls
        }

        @Override
        protected Void doInBackground(DataSnapshot... dataSnapshots) {
            /// task in background
            // get categories of places
            DataSnapshot dataSnapshot = dataSnapshots[0]; /// tis pos[0] is to get from the parameter

            if (dataSnapshot.hasChildren()) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                ArrayList<CategoryPlace> categoriesPlaces = new ArrayList<>();

                for (DataSnapshot ds : children) {

                    CategoryPlace cp = new CategoryPlace();
                    //Log.d(TAG, "doInBackground: " + ds.getKey());
                    cp.setID(ds.getKey());
                    cp.setName(ds.child("name").getValue(String.class));
                    cp.setImageUrl(ds.child("url").getValue(String.class));
                    cp.setReplace(ds.child("replace").getValue(Long.class));
                    //ServerValue.TIMESTAMP
                    Utils.putPicture(getBaseContext(), cp.getName(), cp.getImageUrl(), cp.getReplace());

                    categoriesPlaces.add(cp);
                }

                Utils.saveSharedList(getBaseContext(), Utils.FIRE_DB_CATEGORIES, categoriesPlaces);

            }

            /*for (int i=0; i< 100000000; ++i){

            }*/

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /// redirection!
            if( Utils.readSharedSetting(getBaseContext(), Utils.PREF_FIRST_SYNC, Utils.TRUE).equals(Utils.TRUE) ) {
                Utils.saveSharedSetting(getBaseContext(), Utils.PREF_FIRST_SYNC, Utils.FALSE);

                Intent mainIntent = new Intent(SplashActivity.this, PagerActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }
    }
}

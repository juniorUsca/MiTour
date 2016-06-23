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
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.Timestamp;
import java.util.ArrayList;
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

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

            /*String key = mDatabase.child("categories").push().getKey();

            CategoryPlace p = new CategoryPlace();
            p.setUrl("http://icons.iconarchive.com/icons/icons8/windows-8/128/Travel-Campfire-icon.png");
            p.setName("fuego");

            Map<String, Object> postValues = p.toMap();
            Log.d(TAG, "doInBackground: changing values");

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/categories/" + key, postValues);
            mDatabase.updateChildren(childUpdates);*/


        DatabaseReference categoriesRef = mDatabase.child("categories");
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
            DataSnapshot dataSnapshot = dataSnapshots[0];

            if (dataSnapshot.hasChildren()) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                ArrayList<CategoryPlace> categoriesPlaces = new ArrayList<>();

                for (DataSnapshot ds : children) {

                    CategoryPlace cp = new CategoryPlace();
                    //cp.setImage(ds.child("image").getValue(Integer.class));
                    cp.setName(ds.child("name").getValue(String.class));
                    cp.setUrl(ds.child("url").getValue(String.class));
                    cp.setReplace(ds.child("replace").getValue(Long.class));
                    //ServerValue.TIMESTAMP
                    Utils.putPicture(getBaseContext(), cp.getName(), cp.getUrl(), cp.getReplace());

                    categoriesPlaces.add(cp);
                }

                Utils.saveSharedList(getBaseContext(), "categories", categoriesPlaces);

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

package com.debugcc.mitour.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.debugcc.mitour.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new PrefetchData().execute();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/
    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /// before making http calls
        }

        @Override
        protected Void doInBackground(Void... params) {
            /// task in background
            // get categories of places

            String URL_categoryplace = "https://script.google.com/macros/s/AKfycbxzVgdIhfVGuYZjwxiFokOCMDEvr1hCkIZ0PTdy51BpnKx6F18T/exec";
            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
            RequestFuture<JSONArray> future = RequestFuture.newFuture();
            JsonArrayRequest request = new JsonArrayRequest(URL_categoryplace, future, future);
            requestQueue.add(request);

            try {
                //JSONArray response = future.get(30, TimeUnit.SECONDS);
                JSONArray response = future.get();

                SharedPreferences sharedpreferences = getSharedPreferences("MiTourPREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CategoriesPlaces", response.toString());
                editor.commit();


            } catch (InterruptedException e) {
                Log.e("ERROR1", e.toString());

            } catch (ExecutionException e) {
                Log.e("ERROR2", e.toString());
            }

            /*for (int i=0; i< 100000000; ++i){

            }*/

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /// redirection!
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);

            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
    }
}

package com.debugcc.mitour.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.debugcc.mitour.R;

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
            //JsonParser jsonParser = new JsonParser();

            for (int i=0; i< 100000000; ++i){

            }

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

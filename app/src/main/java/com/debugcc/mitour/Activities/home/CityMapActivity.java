package com.debugcc.mitour.Activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.debugcc.mitour.Activities.MainActivity;
import com.debugcc.mitour.Fragments.home.CityMapFragment;
import com.debugcc.mitour.Models.City;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.PrefUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

public class CityMapActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_city_map_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_city_map_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToMain();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            CityMapFragment fragment = new CityMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.city_map_container, fragment)
                    .commit();
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, PrefUtils.getCurrentUser(CityMapActivity.this).getEmail());
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, PrefUtils.getCurrentUser(CityMapActivity.this).getName());
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, PrefUtils.getCurrentUser(CityMapActivity.this).getServer());
        mFirebaseAnalytics.logEvent("into_CityMapActivity", params);

        Bundle params_where_traveling = new Bundle();
        params_where_traveling.putString(FirebaseAnalytics.Param.ITEM_ID, City.CURRENT_CITY.getID());
        params_where_traveling.putString(FirebaseAnalytics.Param.ITEM_NAME, City.CURRENT_CITY.getCity());
        params_where_traveling.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, City.CURRENT_CITY.getCountry());
        mFirebaseAnalytics.logEvent("traveling", params_where_traveling);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, CityDetailActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    /**
     * REDIRECTIONS -->
     */
    void redirectToMain() {
        Intent intent = new Intent(CityMapActivity.this, MainActivity.class);
        MainActivity.CURRENT_TAB = MainActivity.MAP_TAB;

        this.startActivity(intent);
    }
}

package com.debugcc.mitour.Activities.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.debugcc.mitour.Activities.MainActivity;
import com.debugcc.mitour.Fragments.home.CityDetailFragment;
import com.debugcc.mitour.Fragments.home.CityMapFragment;
import com.debugcc.mitour.Fragments.home.HomeFragment;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.PrefUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;

public class CityDetailActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.city_detail_detail_toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab_map_top = (FloatingActionButton) findViewById(R.id.city_detail_fab_map_top);
        final FloatingActionButton fab_map_bottom = (FloatingActionButton) findViewById(R.id.city_detail_fab_map_bottom);

        fab_map_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToMap();
            }
        });

        fab_map_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToMap();
            }
        });


        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.city_detail_app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                /**
                 * verticalOffset changes in diapason
                 * from 0 - appBar is fully unwrapped
                 * to -appBarLayout's height - appBar is totally collapsed
                 * so in example we hide FAB when user folds half of the appBarLayout
                 */
                if (appBarLayout.getHeight() / 2 < -verticalOffset) {
                    fab_map_bottom.setVisibility(View.VISIBLE);
                    //fab_map_top.setVisibility(View.GONE);
                } else {
                    //fab_map_top.setVisibility(View.VISIBLE);
                    fab_map_bottom.setVisibility(View.GONE);
                }
            }
        });



        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            //Bundle arguments = new Bundle();
            //arguments.putString(CityDetailFragment.ARG_CITY_ID,
                    //getIntent().getStringExtra(CityDetailFragment.ARG_CITY_ID));
            CityDetailFragment fragment = new CityDetailFragment();
            //fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.city_detail_container, fragment)
                    .commit();
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, PrefUtils.getCurrentUser(CityDetailActivity.this).getEmail());
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, PrefUtils.getCurrentUser(CityDetailActivity.this).getName());
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, PrefUtils.getCurrentUser(CityDetailActivity.this).getServer());
        mFirebaseAnalytics.logEvent("into_CityDetailActivity", params);

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
            //navigateUpTo(new Intent(this, ItemListActivity.class));
            MainActivity.CURRENT_TAB = MainActivity.HOME_TAB;
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void redirectToMap() {
        Intent intent = new Intent(this, CityMapActivity.class);
        //intent.putExtra (CityDetailFragment.ARG_CITY_ID,
                //getIntent().getStringExtra(CityDetailFragment.ARG_CITY_ID) );

        this.startActivity(intent);
    }

}

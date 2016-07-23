package com.debugcc.mitour.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.debugcc.mitour.Fragments.home.HomeFragment;
import com.debugcc.mitour.Fragments.main.MapsFragment;
import com.debugcc.mitour.Models.User;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.PrefUtils;
import com.debugcc.mitour.utils.Utils;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    public static int HOME_TAB = 0;
    public static int MAP_TAB = 1;
    public static int CURRENT_TAB = HOME_TAB;


    private FirebaseAnalytics mFirebaseAnalytics;

    FloatingActionButton fab_main;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        fab_main = (FloatingActionButton) findViewById(R.id.main_fab);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CURRENT_TAB = MAP_TAB;
                chargeCurrentTab();
            }
        });

        /// charge data from Logged
        chargeProfile();

        chargeCurrentTab();



        /*final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_vector);
        }*/


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, PrefUtils.getCurrentUser(MainActivity.this).getEmail());
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, PrefUtils.getCurrentUser(MainActivity.this).getName());
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, PrefUtils.getCurrentUser(MainActivity.this).getServer());
        mFirebaseAnalytics.logEvent("into_MainActivity", params);

    }

    /**
     * CHARGE CURRENT FRAGMENT
     */
    private void chargeCurrentTab() {
        Fragment genericFragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (CURRENT_TAB == HOME_TAB) {
            fab_main.setVisibility(View.VISIBLE);
            genericFragment = new HomeFragment();
        }
        if (CURRENT_TAB == MAP_TAB) {
            fab_main.setVisibility(View.GONE);
            genericFragment = new MapsFragment();
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.container_main, genericFragment)
                .commit();

        MenuItem item = mNavigationView.getMenu().getItem( CURRENT_TAB );
        setTitle(item.getTitle());
        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void chargeProfile() {
        View header = mNavigationView.getHeaderView(0);

        final ImageView user_profile = (ImageView) header.findViewById(R.id.user_picture);
        TextView user_name = (TextView) header.findViewById(R.id.user_name);
        TextView user_email = (TextView) header.findViewById(R.id.user_email);

        User user = PrefUtils.getCurrentUser(MainActivity.this);

        Glide.with(this)
                .load(user.getUrlProfilePicture())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.ic_action_user)
                .into(new BitmapImageViewTarget(user_profile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                user_profile.setImageDrawable(circularBitmapDrawable);
            }
        });

        user_name.setText(user.getName());
        user_email.setText(user.getEmail());

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment genericFragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fab_main.setVisibility(View.VISIBLE);
            genericFragment = HomeFragment.newInstance("hola1","hola2");
        } else if (id == R.id.nav_map) {
            fab_main.setVisibility(View.GONE);
            genericFragment = MapsFragment.newInstance("hola1","hola2");
            //fab_main.setImageResource(R.drawable.ic_my_location_vector);
            //fab_main.show();
        /*} else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
*/
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }


        if (genericFragment != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_main, genericFragment)
                    .commit();
        }

        setTitle(item.getTitle());
        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    /**
     * INTERACTION WITH FRAGMENTS
     */
    @Override
    public void onCitySelected() {

        /*MapsFragment mapsFrag = (MapsFragment)
                getSupportFragmentManager().findFragmentById(R.id.maps_fragment);

        if (mapsFrag != null) {
            Log.e(TAG, "onCitySelected: EXISTE EL FRAGMENTO MAP");
            //mapsFrag.updateView();
        } else {
            Fragment genericFragment = MapsFragment.newInstance("hola1","hola2");

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_main, genericFragment, "Inicio")
                    .addToBackStack("Inicio")
                    .commit();
        }

        MenuItem item = mNavigationView.getMenu().getItem(2);
        setTitle(item.getTitle());
        item.setChecked(true);*/
    }
}

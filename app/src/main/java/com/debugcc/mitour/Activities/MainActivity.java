package com.debugcc.mitour.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import com.debugcc.mitour.Fragments.home.HomeFragment;
import com.debugcc.mitour.Fragments.main.MapsFragment;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static int ZERO = 0;
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

        fab_main = (FloatingActionButton) findViewById(R.id.fab_mylocation);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /// charge data from Logged
        Utils.chargeDataLoged();


        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.container_main,new HomeFragment())
                .commit();

        MenuItem item = mNavigationView.getMenu().getItem(ZERO);
        setTitle(item.getTitle());
        item.setChecked(true);


        /*final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_vector);
        }*/

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

        fab_main.hide();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            genericFragment = HomeFragment.newInstance("hola1","hola2");
        } else if (id == R.id.nav_map) {
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

package com.debugcc.mitour.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.PrefUtils;
import com.facebook.login.LoginManager;

public class SettingsActivity extends AppCompatActivity {

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.settings_container, new SettingsFragment());
        ft.commit();
        //addToolbar();


        btnLogout = (Button) findViewById(R.id.logout_button);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.clearCurrentUser(SettingsActivity.this);

                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();

                Intent i= new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
    }


    public static class SettingsFragment extends PreferenceFragment {

        public SettingsFragment() {
            // Constructor Por Defecto
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //addPreferencesFromResource(R.xml.preferencias);
        }
    }
}

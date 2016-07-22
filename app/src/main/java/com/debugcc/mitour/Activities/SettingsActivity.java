package com.debugcc.mitour.Activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.debugcc.mitour.Models.User;
import com.debugcc.mitour.MyApplication;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.PrefUtils;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {//implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<People.LoadPeopleResult> {

    private static final String TAG = "SettingsActivity";

    private FirebaseAnalytics mFirebaseAnalytics;

    private Button btnLogout;
    private User mUser;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.settings_container, new SettingsFragment());
        ft.commit();
        //addToolbar();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mUser = PrefUtils.getCurrentUser(SettingsActivity.this);

        btnLogout = (Button) findViewById(R.id.logout_button);
        if (mUser != null) {
            if (mUser.server.equals(User.FACEBOOK_SERVER))
                btnLogout.setBackgroundColor( getResources().getColor(R.color.com_facebook_button_background_color) );
            if (mUser.server.equals(User.GOOGLE_SERVER))
                btnLogout.setBackgroundColor( getResources().getColor(R.color.red) );
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(SettingsActivity.this);

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID, PrefUtils.getCurrentUser(SettingsActivity.this).getEmail());
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, PrefUtils.getCurrentUser(SettingsActivity.this).getName());
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, PrefUtils.getCurrentUser(SettingsActivity.this).getServer());
        mFirebaseAnalytics.logEvent("into_SettingsActivity", params);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /// ANALYTICS
                Bundle params = new Bundle();
                params.putString(FirebaseAnalytics.Param.ITEM_ID, PrefUtils.getCurrentUser(SettingsActivity.this).getEmail());
                params.putString(FirebaseAnalytics.Param.ITEM_NAME, PrefUtils.getCurrentUser(SettingsActivity.this).getName());
                params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, PrefUtils.getCurrentUser(SettingsActivity.this).getServer());
                mFirebaseAnalytics.logEvent("logout", params);
                /// END Analytics

                PrefUtils.clearCurrentUser(SettingsActivity.this);

                Log.e(TAG, "onClick: " + mUser.server);

                if (mUser.server.equals(User.FACEBOOK_SERVER)) {
                    // We can logout from facebook by calling following method
                    LoginManager.getInstance().logOut();
                }

                if (mUser.server.equals(User.GOOGLE_SERVER)) {

                    Log.e(TAG, "onClick: GOOGLE LOGOUT " + mGoogleApiClient.isConnected());

                    Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(@NonNull Status status) {
                                    // ...
                                    Log.e(TAG, "onResult: AKIIII" );
                                    Log.e(TAG, "onResult: " + status.toString() );
                                }
                            }
                    );
                }

                FirebaseAuth.getInstance().signOut();

                Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    private void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.e(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            //handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            Log.e(TAG, "NOO Got cached sign-in LOGUEATE");
            /*showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });*/
        }
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







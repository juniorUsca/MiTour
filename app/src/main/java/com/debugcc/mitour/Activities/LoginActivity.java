package com.debugcc.mitour.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.debugcc.mitour.Models.User;
import com.debugcc.mitour.MyApplication;
import com.debugcc.mitour.R;
import com.debugcc.mitour.utils.PrefUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = "LoginActivity";

    private User mUser;
    private ProgressDialog mProgressDialog;

    ///firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /// google
    private SignInButton mSignInGoogleButton;
    private Button mLoginButtonGoogle;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 9001;
    /// facebook
    private LoginButton mLoginFacebookButton;
    private Button mLoginButtonFacebook;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            handleFacebookAccessToken(loginResult.getAccessToken());

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted (JSONObject object, GraphResponse response) {
                            Log.e("response: ", response + "");
                            Log.e("response OBJECT: ", object.toString() + "");
                            mProgressDialog.dismiss();
                            mUser = new User();
                            try {
                                if (object.has("id")) {
                                    mUser.userID = object.getString("id");
                                    mUser.urlProfilePicture = "https://graph.facebook.com/" + mUser.userID + "/picture?type=large";
                                }
                                if (object.has("email"))
                                    mUser.email = object.getString("email");
                                if (object.has("name"))
                                    mUser.name = object.getString("name");
                                //mUser.gender = object.getString("gender");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            mUser.server = User.FACEBOOK_SERVER;
                            mUser.print();
                            PrefUtils.setCurrentUser(mUser,LoginActivity.this);

                            Toast.makeText(LoginActivity.this,"Bienvenido "+mUser.name,Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            LoginActivity.this.finish();
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            // App code
            mProgressDialog.dismiss();
            Log.e(TAG, "onCancel: " );
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            mProgressDialog.dismiss();
            Log.e(TAG, "onError: " + exception.toString() );
        }
    };

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete: LOGUEADO CON FACEBOOK" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppEventsLogger.activateApp(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (PrefUtils.getCurrentUser(LoginActivity.this) != null) {
            Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getPhotoUrl());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getDisplayName());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        /// facebook

        mCallbackManager = CallbackManager.Factory.create();

        mLoginFacebookButton = (LoginButton) findViewById(R.id.login_facebook_button);
        if (mLoginFacebookButton != null) {
            mLoginFacebookButton.setReadPermissions("public_profile", "email", "user_friends", "contact_email");
        }

        mLoginButtonFacebook = (Button) findViewById(R.id.login_buttonfa);
        mLoginButtonFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage("Cargando...");
                mProgressDialog.show();

                mLoginFacebookButton.performClick();
                mLoginFacebookButton.setPressed(true);
                mLoginFacebookButton.invalidate();
                mLoginFacebookButton.registerCallback(mCallbackManager, mCallBack);
                mLoginFacebookButton.setPressed(false);
                mLoginFacebookButton.invalidate();
            }
        });

        /// google

        mSignInGoogleButton = (SignInButton) findViewById(R.id.login_google_button);
        mSignInGoogleButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInGoogleButton.setScopes(gso.getScopeArray());

        mLoginButtonGoogle = (Button) findViewById(R.id.login_buttongo);
        mLoginButtonGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage("Cargando...");
                mProgressDialog.show();

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // facebook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        
        /// google
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            mProgressDialog.dismiss();

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount(); // account
            if (acct != null) {

                firebaseAuthWithGoogle(acct);

                mUser = new User();
                mUser.userID = acct.getId();
                mUser.email = acct.getEmail();
                mUser.name = acct.getDisplayName();
                if( acct.getPhotoUrl() != null)
                    mUser.urlProfilePicture = acct.getPhotoUrl().toString();
                mUser.server = User.GOOGLE_SERVER;

                mUser.print();

                PrefUtils.setCurrentUser(mUser,LoginActivity.this);

                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }


        } else {
            mProgressDialog.dismiss();

            Log.e(TAG, "handleSignInResult: " + "NO LOGUEO GOOGLE" );
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete: LOGUEADO CON GOOGLE" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mProgressDialog.dismiss();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}



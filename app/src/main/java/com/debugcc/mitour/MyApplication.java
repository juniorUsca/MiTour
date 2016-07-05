package com.debugcc.mitour;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dubgcc on 10/06/16.
 */
// MultiDexApplication
public class MyApplication extends Application {

    private static final String TAG = "MiTourAPP";

    @Override
    public void onCreate() {
        super.onCreate();

        /// Initialize Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.e("MiTour", "Iniciando APP");
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.debugcc.mitour",  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "onCreate1: ", e);

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "onCreate2: ", e);
        }
    }

}

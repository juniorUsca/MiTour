package com.debugcc.mitour;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
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

        /* Use on Async Task

        AdvertisingIdClient.Info idInfo = null;
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }*/
    }

}

package com.credera.pushnotificationspike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.credera.pushnotificationspike.gcm.SpikeGcmRegistrationIntentService;
import com.credera.pushnotificationspike.services.GooglePlayService;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register the app with push notifications
        if (GooglePlayService.checkPlayServices(this, false)) {
            final Intent intent = new Intent(this, SpikeGcmRegistrationIntentService.class);
            startService(intent);
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }
}

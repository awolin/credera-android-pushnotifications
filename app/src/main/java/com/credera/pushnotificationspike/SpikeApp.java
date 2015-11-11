package com.credera.pushnotificationspike;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by aaronwolin on 11/10/15.
 */
public class SpikeApp extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String NOTIFICATION_LIST = "notification_list";

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);
    }


    /*
     * Application.ActivityLifecycleCallbacks
     */

    @Override
    public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(final Activity activity) {
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        clearSavedNotifications();
    }

    @Override
    public void onActivityPaused(final Activity activity) {
    }

    @Override
    public void onActivityStopped(final Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(final Activity activity) {
    }


    private void clearSavedNotifications() {
        try {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            final SharedPreferences.Editor e = prefs.edit();
            e.remove(NOTIFICATION_LIST);
            e.apply();
        } catch (Exception e) {
            // Nothing.
        }
    }
}
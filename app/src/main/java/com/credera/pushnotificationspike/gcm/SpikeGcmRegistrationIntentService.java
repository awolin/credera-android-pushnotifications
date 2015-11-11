package com.credera.pushnotificationspike.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.credera.pushnotificationspike.Constants;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aaronwolin on 11/8/15.
 */
public class SpikeGcmRegistrationIntentService extends IntentService {

    public static final String TAG = SpikeGcmRegistrationIntentService.class.getSimpleName();

    private static final String PREFS_REGISTRATION_ID = "RegistrationID";
    private static final String PREFS_ACCOUNT_ID = "AccountID";

    private static final String PREFS_REGISTRATION_COMPLETE = "registrationComplete";


    public SpikeGcmRegistrationIntentService() {
        super(TAG);
    }

    /**
     * @return user's Account ID or null if it's empty
     */
    public String getRegistrationID() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String registrationId = sharedPreferences.getString(PREFS_REGISTRATION_ID, "");

        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration ID not found.");
            return "";
        }

        return registrationId;
    }

    /**
     * @return user's Account ID or null if it's empty
     */
    public String getAccountId() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String memberId = sharedPreferences.getString(PREFS_ACCOUNT_ID, null);

        if (memberId == null) {
            Log.i(TAG, "Account ID not found.");
            return "";
        }

        return memberId;
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            final String deviceId = sharedPreferences.getString(PREFS_REGISTRATION_ID, null);
            if (deviceId != null) {
                Log.i(TAG, "GCM registration token already sent:\n" + deviceId);
                return;
            }

            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                final InstanceID instanceID = InstanceID.getInstance(this);
                final String token = instanceID.getToken(Constants.GoogleCloudMessaging.ID,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                // Log the Token for debugging purposes
                Log.i(TAG, "GCM Registration Token:\n" + token);

                // Send the token to 3rd-party servers
                sendRegistrationToServer(token);

                // You should store the token for debugging purposes and to make sure the token has been
                // sent to the server
                sharedPreferences.edit().putString(PREFS_REGISTRATION_ID, token).apply();
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);

            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putString(PREFS_REGISTRATION_ID, null).apply();
        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        final Intent registrationComplete = new Intent(PREFS_REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        Log.i(TAG, "Registering device: " + token);

        final Map<String, String> postParams = new HashMap<>();
        postParams.put("deviceUniqueId", token);
        postParams.put("deviceType", "Android");
        postParams.put("accountId", getAccountId());

        // TODO: Send registration information to the push processing/storage server
    }
}

package com.credera.pushnotificationspike.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by aaronwolin on 11/8/15.
 */
public class SpikeGcmInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = SpikeGcmInstanceIDListenerService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        final Intent intent = new Intent(this, SpikeGcmRegistrationIntentService.class);
        startService(intent);
    }
}

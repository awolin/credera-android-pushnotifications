package com.credera.pushnotificationspike.services;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.credera.pushnotificationspike.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by aaronwolin on 11/9/15.
 */
public class GooglePlayService {

    private static boolean sGooglePlayWarningOpened = false;
    private static int sGooglePlayServicesAvailable;

    private static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    private static final String PREF_USER_IGNORED_GOOGLE_PLAY_SERVICES_WARNING = "google_play_services_location_ignored";

    public static boolean checkPlayServices(final Activity context, final boolean forceWarning) {
        sGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        final boolean googlePlayServiceWarningIgnored = sp.getBoolean(PREF_USER_IGNORED_GOOGLE_PLAY_SERVICES_WARNING, forceWarning);

        if (sGooglePlayServicesAvailable != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(sGooglePlayServicesAvailable)) {
                // Display the warning if the user has not seen it before, or if the  warning is forced
                if ((!googlePlayServiceWarningIgnored || forceWarning) && !sGooglePlayWarningOpened) {
                    sGooglePlayWarningOpened = true;

                    final AlertDialog dialog = (AlertDialog) GooglePlayServicesUtil.getErrorDialog(
                            sGooglePlayServicesAvailable, context, REQUEST_CODE_RECOVER_PLAY_SERVICES);

                    // Customize the GooglePlayServicesUtil dialog
                    dialog.setMessage(context.getString(R.string.error_google_services_missing_push));
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                            context.getString(R.string.ignore),
                            new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int which) {
                                    // Save the flag that we've ignored this dialog
                                    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                                    sp.edit().putBoolean(PREF_USER_IGNORED_GOOGLE_PLAY_SERVICES_WARNING, true).apply();

                                    dialog.cancel();
                                }
                            });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            sGooglePlayWarningOpened = false;
                        }
                    });

                    dialog.show();
                }
            } else {
                Toast.makeText(context, R.string.error_device_not_supported, Toast.LENGTH_LONG).show();
                context.finish();
            }

            return false;
        }

        return true;
    }

    public static int isGooglePlayServiceAvailable() {
        return sGooglePlayServicesAvailable;
    }
}

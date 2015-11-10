package com.credera.pushnotificationspike.gcm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.credera.pushnotificationspike.MainActivity;
import com.credera.pushnotificationspike.R;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by aaronwolin on 11/8/15.
 */
public class SpikeGcmListenerService extends GcmListenerService {

    public static final String TAG = SpikeGcmListenerService.class.getSimpleName();

    private static final String NOTIFICATION_LIST = "notification_list";

    private static final String GROUP_MESSAGES = "group_messages";
    private static final Random sRandIdGenerator = new Random();
    private static final int sNotificationIconColor = Color.parseColor("#C52033");


    @Override
    public void onMessageReceived(final String from, final Bundle data) {
        final String message = data.getString("message");
        final String title = data.getString("title");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(title, message);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String title, String message) {

        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

        final NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this.getApplicationContext());

        if (title == null) {
            title = "Spike App";
        }

        final NotificationData newNotification = new NotificationData(sRandIdGenerator.nextInt(), title, message);
        final List<NotificationData> notificationList = retrievePreviousNotifications();
        notificationList.add(newNotification);

        if (notificationList.size() == 1) {
            // Set the message notification
            final NotificationCompat.Builder messageNotification = new NotificationCompat.Builder(this)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setGroup(GROUP_MESSAGES)
                    .setSmallIcon(R.drawable.ic_credera_bar)
                    .setColor(sNotificationIconColor)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(contentIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

            if (Build.VERSION.SDK_INT >= 21) {
                messageNotification.setCategory(Notification.CATEGORY_PROMO);
            }

            notificationManager.notify(newNotification.id, messageNotification.build());
        } else {
            notificationManager.cancelAll();

            NotificationCompat.InboxStyle summaryStyle = new NotificationCompat.InboxStyle();

            for (NotificationData notification : notificationList) {
                summaryStyle.addLine(notification.message);
            }

            summaryStyle.setBigContentTitle(notificationList.size() + " new updates");
            summaryStyle.setSummaryText("Spike App");

            final NotificationCompat.Builder summaryNotification = new NotificationCompat.Builder(this)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setGroupSummary(true)
                    .setAutoCancel(true)
                    .setGroup(GROUP_MESSAGES)
                    .setSmallIcon(R.drawable.ic_credera_bar)
                    .setColor(sNotificationIconColor)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(contentIntent)
                    .setStyle(summaryStyle);

            if (Build.VERSION.SDK_INT >= 21) {
                summaryNotification.setCategory(Notification.CATEGORY_PROMO);
            }

            notificationManager.notify(sRandIdGenerator.nextInt(), summaryNotification.build());
        }

        saveNotifications(notificationList);
    }

    private List<NotificationData> retrievePreviousNotifications() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        final String notificationListString = prefs.getString(NOTIFICATION_LIST, null);

        List<NotificationData> notificationList = new ArrayList<>();

        if (notificationListString != null) {
            final Gson gson = new Gson();

            Type listType = new TypeToken<List<NotificationData>>() {
            }.getType();

            notificationList = gson.fromJson(notificationListString, listType);
        }

        return notificationList;
    }

    private void saveNotifications(final List<NotificationData> notificationList) {
        final StringBuilder notificationListJson = new StringBuilder();
        notificationListJson.append("[");

        for (int i = 0; i < notificationList.size(); i++) {
            final Gson gson = new Gson();
            notificationListJson.append(gson.toJson(notificationList.get(i)));

            if (i < notificationList.size() - 1) {
                notificationListJson.append(",");
            }
        }

        notificationListJson.append("]");

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        final SharedPreferences.Editor e = prefs.edit();
        e.putString(NOTIFICATION_LIST, notificationListJson.toString());
        e.apply();
    }
}
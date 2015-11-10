package com.credera.pushnotificationspike.gcm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aaronwolin on 11/8/15.
 */
public class NotificationData implements Parcelable {

    public int id;

    public String title;

    public String message;


    /**
     * Default constructor
     */
    public NotificationData() {
    }

    /**
     * Constructor
     */
    public NotificationData(final int id, final String title, final String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    /**
     * Parcelable constructor
     *
     * @param in parcel
     */
    NotificationData(final Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.message = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.message);
    }

    public static final Creator<NotificationData> CREATOR
            = new Creator<NotificationData>() {

        public NotificationData createFromParcel(final Parcel in) {
            return new NotificationData(in);
        }

        public NotificationData[] newArray(final int size) {
            return new NotificationData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
package io.paysky.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PaySky-3 on 8/13/2017.
 */

public class TrackData implements Parcelable {
    String track1;
    public String cardHolderName;
    public String track2;
    public String cardNumber;
    public String expiryDate;


    protected TrackData(Parcel in) {
        track1 = in.readString();
        track2 = in.readString();
        cardNumber = in.readString();
        expiryDate = in.readString();
        cardHolderName = in.readString();
    }

    public static final Creator<TrackData> CREATOR = new Creator<TrackData>() {
        @Override
        public TrackData createFromParcel(Parcel in) {
            return new TrackData(in);
        }

        @Override
        public TrackData[] newArray(int size) {
            return new TrackData[size];
        }
    };

    public String getTrack2() {
        return track2;
    }


    TrackData() {

    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(track1);
        parcel.writeString(track2);
        parcel.writeString(cardNumber);
        parcel.writeString(expiryDate);
        parcel.writeString(cardHolderName);
    }
}

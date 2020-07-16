package edu.miracostacollege.cs134.caffeinefinder.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The <code>Location</code> class represents a place where one can get a caffeine fix, including
 * its name, address, phone number and latitude/longitude location.
 *
 * @author Michael Paulding
 */

public class Location implements Parcelable {
    private long mId;
    private String mName;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mZipCode;
    private String mPhone;
    private double mLatitude;
    private double mLongitude;

    public Location(long id, String name, String address, String city, String state, String zipCode, String phone, double latitude, double longitude) {
        mId = id;
        mName = name;
        mAddress = address;
        mCity = city;
        mState = state;
        mZipCode = zipCode;
        mPhone = phone;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public Location(String name, String address, String city, String state, String zipCode, String phone, double latitude, double longitude) {
        this(-1, name, address, city, state, zipCode, phone, latitude, longitude);
    }

    protected Location(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mAddress = in.readString();
        mCity = in.readString();
        mState = in.readString();
        mZipCode = in.readString();
        mPhone = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getFullAddress()
    {
        return mAddress + "\n" + mCity + ", " + mState + "  " + mZipCode;
    }

    public String getFormattedLatLng()
    {
        String latLng = String.valueOf(Math.abs(mLatitude));
        latLng += ((mLatitude < 0.0) ? " S  " : " N  ");
        latLng += String.valueOf(Math.abs(mLongitude));
        latLng += ((mLongitude < 0.0) ? " W" : "E");
        return latLng;
    }

    @Override
    public String toString() {
        return "Location{" +
                "Id=" + mId +
                ", Name='" + mName + '\'' +
                ", Address='" + mAddress + '\'' +
                ", City='" + mCity + '\'' +
                ", State='" + mState + '\'' +
                ", Zip Code='" + mZipCode + '\'' +
                ", Phone='" + mPhone + '\'' +
                ", Latitude=" + mLatitude +
                ", Longitude=" + mLongitude +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mName);
        parcel.writeString(mAddress);
        parcel.writeString(mCity);
        parcel.writeString(mState);
        parcel.writeString(mZipCode);
        parcel.writeString(mPhone);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}

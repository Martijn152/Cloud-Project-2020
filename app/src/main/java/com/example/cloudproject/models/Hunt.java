package com.example.cloudproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Hunt implements Parcelable {
    private String title;
    private String description;
    private String area;
    private String owner;
    private ArrayList<Location> locations;

    public Hunt(String title, String description, String area, String owner, ArrayList<Location> locations) {
        this.title = title;
        this.description = description;
        this.area = area;
        this.owner = owner;
        this.locations = locations;
    }

    protected Hunt(Parcel in) {
        title = in.readString();
        description = in.readString();
        area = in.readString();
        owner = in.readString();
    }

    public static final Creator<Hunt> CREATOR = new Creator<Hunt>() {
        @Override
        public Hunt createFromParcel(Parcel in) {
            return new Hunt(in);
        }

        @Override
        public Hunt[] newArray(int size) {
            return new Hunt[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(area);
        parcel.writeString(owner);
    }
}

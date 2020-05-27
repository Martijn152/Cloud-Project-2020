package com.example.cloudproject.models;

import java.util.ArrayList;

public class Hunt {
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
}

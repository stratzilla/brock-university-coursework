package com.example.b.bc;

public class Rooms {
    String name;
    double lat;
    double lng;
    int radius;

    // constructors

    public void Rooms() {}

    public void Rooms(String name, double lat, double lng, int radius) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
    }

    // getter methods

    public String getName() {
        return name;
    }

    public double getLat() { return lat; }

    public double getLng() {
        return lng;
    }

    public int getRadius() {
        return radius;
    }
}

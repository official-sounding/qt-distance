package com.officialsounding.qtstats;

public class QT {

   private String intersection;
   private double latitude;
   private double longitude;

    private double latRadians;
    public double longRadians;

    private double x;
    private double y;

    public String getIntersection() {
        return intersection;
    }

    public void setIntersection(String intersection) {
        this.intersection = intersection;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        this.latRadians =  Math.toRadians(latitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        this.longRadians = Math.toRadians(longitude);
    }

    public double getLongRadians() {
        return longRadians;
    }

    public double getLatRadians() {
       return latRadians;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
 }

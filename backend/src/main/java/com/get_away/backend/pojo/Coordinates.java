package com.get_away.backend.pojo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;

/**
 * Created by rrk on 5/7/16.
 */

@Entity
public class Coordinates implements Serializable{

    @Id Long id;

    public Coordinates() {
    }

    public Coordinates(String lat, String lon) {
        latitude = lat;
        longitude = lon;
    }

    public String latitude="";
    public String longitude="";

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}

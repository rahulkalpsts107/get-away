package com.get_away.backend.pojo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rrk on 5/6/16.
 */

@Entity
public class User implements Serializable{

    @Id Long id;

    public String fsqName="";
    public String email="";
    public String imageUrl="";
    public String gcmToken="";

    @Index
    public String fsqId="";

    public Coordinates getCurrentLoc() {
        return currentLoc;
    }

    public void setCurrentLoc(Coordinates currentLoc) {
        this.currentLoc = currentLoc;
    }

    public Coordinates currentLoc = new Coordinates();



    public List<Recommendation> recommendations = new ArrayList<Recommendation>();

    public Long getId() {
        return id;
    }

    public String getFsqName() {
        return fsqName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public String getFsqId() {
        return fsqId;
    }

    public void setFsqName(String name) {
        fsqName = name;
    }

    public void setEmail(String em) {
        email = em;
    }

    public void setImageUrl(String url) {
        imageUrl = url;
    }

    public void setFsqID(String id) {
        fsqId = id;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fsqName='" + fsqName + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", gcmToken='" + gcmToken + '\'' +
                ", fsqId='" + fsqId + '\'' +
                ", currentLoc=" + currentLoc +
                ", recommendations=" + recommendations +
                '}';
    }

    public User() {
    }

    public User( String fsqName, String email, String imageUrl, String gcmToken, String fsqId , String lat, String longi){
        this.fsqName = fsqName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.gcmToken = gcmToken;
        this.fsqId = fsqId;
        Coordinates cor = new Coordinates();
        cor.setLatitude(lat);
        cor.setLongitude(longi);
        this.currentLoc= cor;
    }
}

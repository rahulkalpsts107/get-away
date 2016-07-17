package com.get_away.backend.pojo;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rrk on 5/6/16.
 */

@Entity
public class Recommendation implements Serializable{

    @Id Long id;

    public String placeName;
    public Coordinates location;
    public String address;
    public String categoryId;
    public String categoryName;
    public String contact;

    public int getCheckinsCount() {
        return checkinsCount;
    }

    public void setCheckinsCount(int checkinsCount) {
        this.checkinsCount = checkinsCount;
    }

    public int checkinsCount;

    public String getFriendCheckedIn() {
        return friendCheckedIn;
    }

    public void setFriendCheckedIn(String friendCheckedIn) {
        this.friendCheckedIn = friendCheckedIn;
    }

    public String friendCheckedIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Coordinates getLocation() {
        return location;
    }

    public void setLocation(Coordinates location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", placeName='" + placeName + '\'' +
                ", location=" + location +
                ", address='" + address + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", contact='" + contact + '\'' +
                ", checkInsCount'" + checkinsCount + '\'' +
                ", friendsCheckedIn=" + friendCheckedIn +
                '}';
    }
}

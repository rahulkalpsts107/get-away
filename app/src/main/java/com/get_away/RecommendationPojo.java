package com.get_away;

import java.io.Serializable;

/**
 * Created by rrk on 5/6/16.
 */

public class RecommendationPojo implements Serializable{

Long id;

    public String placeName;
    public CoordinatesPojo location;
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

    public CoordinatesPojo getLocation() {
        return location;
    }

    public void setLocation(CoordinatesPojo location) {
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

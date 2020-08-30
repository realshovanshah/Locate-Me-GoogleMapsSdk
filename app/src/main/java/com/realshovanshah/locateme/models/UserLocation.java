package com.realshovanshah.locateme.models;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserLocation {
    private GeoPoint geoLocation;
    private User user;
    private @ServerTimestamp Date timeStamp;

    public UserLocation() {
    }

    public UserLocation(GeoPoint geoLocation, User user, Date timeStamp) {
        this.geoLocation = geoLocation;
        this.user = user;
        this.timeStamp = timeStamp;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}

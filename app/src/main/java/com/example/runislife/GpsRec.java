package com.example.runislife;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;

public class GpsRec implements Serializable {
    public double lat, lng, alt;
    public double distance;
    public double speed;
    public Date date;
    transient public Location loc;

    public GpsRec() {
    }

    public GpsRec(Date date, Location l) {
        super();
        this.lat = l.getLatitude();
        this.lng = l.getLongitude();
        this.alt = l.getAltitude();
        this.date = date;
        this.loc = l;
    }
    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public double getAlt() {
        return alt;
    }

    public Date getDate() {
        return date;
    }
    @Override
    public String toString() {
        return String.format("Location: %s, %.6f, %.6f, %.2f, dist=%.2f, sp=%f", date, lat, lng, alt, distance, speed);
    }

}

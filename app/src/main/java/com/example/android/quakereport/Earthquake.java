package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.security.AccessController.getContext;

public class Earthquake {

    private String mLocation;
    private double mMagnitude;
    private long mTime;
    private String mUrl;

    public Earthquake(String location, double magnitude, long time, String url) {
        this.mLocation = location;
        this.mMagnitude = magnitude;
        this.mTime = time;
        this.mUrl = url;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public void setMagnitude(double magnitude) {
        this.mMagnitude = magnitude;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public String getmUrl() {
        return mUrl;
    }

    /**
     * Method for converting unix timestamp to human readable date
     *
     * @return String date
     */

    public String unixTimeToDate() {

        Date date = new Date(mTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. MMM. yyyy.");

        return simpleDateFormat.format(date);
    }

    /**
     * Method for converting unix time to human readable time
     *
     * @return
     */

    public String unixTimeToTime() {

        Date date = new Date(mTime);
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("kk:mm:ss");

        return simpleTimeFormat.format(date);
    }
}

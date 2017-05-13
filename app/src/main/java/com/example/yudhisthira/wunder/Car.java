package com.example.yudhisthira.wunder;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This class single car information
 */

public class Car implements Serializable{

    @SerializedName("name")
    private String      mCarName;

    @SerializedName("address")
    private String      mCarAddress;

    @SerializedName("coordinates")
    private List<Double> mLocation;

    @SerializedName("engineType")
    private String      mCarEngineType;

    @SerializedName("exterior")
    private String      mCarExterior;

    @SerializedName("interior")
    private String      mCarInterior;

    @SerializedName("vin")
    private String      mCarVIn;

    @SerializedName("fuel")
    private int         mCarFuel;


    private double      mLatitude;
    private double      mLongitude;

    public Car(String name, String address, double lat, double lng) {
        mCarName = name;
        mCarAddress = address;
        mLatitude = lat;
        mLongitude = lng;
    }

    public String getCarName() {
        return mCarName;
    }

    public String getCarAddress() {
        return mCarAddress;
    }

    public double getLatitude() {
        Double d = mLocation.get(1);

        return d;
    }

    public double getLongitude() {
        Double d = mLocation.get(0);

        return d;
    }

}

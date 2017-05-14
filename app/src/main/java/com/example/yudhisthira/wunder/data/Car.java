package com.example.yudhisthira.wunder.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This class single car information. GSON will serialize Car information into this class
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

    /**
     *
     */
    private double      mLatitude;

    /**
     *
     */
    private double      mLongitude;

    public Car(String name, String address, double lat, double lng) {
        mCarName = name;
        mCarAddress = address;
        mLatitude = lat;
        mLongitude = lng;
    }

    /**
     *
     * @return String name of car
     */
    public String getCarName() {
        return mCarName;
    }

    /**
     *
     * @return String address of Car
     */
    public String getCarAddress() {
        return mCarAddress;
    }

    /**
     *
     * @return latitude of car location
     */
    public double getLatitude() {
        Double d = mLocation.get(1);

        return d;
    }

    /**
     *
     * @return longitude of car location
     */
    public double getLongitude() {
        Double d = mLocation.get(0);

        return d;
    }

}

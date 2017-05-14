package com.example.yudhisthira.wunder.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yudhisthira on 12/05/17.
 *
 * This class holds the list of Car response. GSON will serialize response into this
 */
public class MyCarList {
    @SerializedName("placemarks")
    private List<Car>  carList;

    /**
     *
     * @return List of Car objects
     */
    public List<Car> getCarList() {
        return carList;
    }
}

package com.example.yudhisthira.wunder;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yudhisthira on 12/05/17.
 */

public class MyCarList {
    @SerializedName("placemarks")
    private List<Car>  carList;

    public List<Car> getCarList() {
        return carList;
    }
}

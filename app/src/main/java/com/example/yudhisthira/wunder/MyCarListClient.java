package com.example.yudhisthira.wunder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by yudhisthira on 12/05/17.
 */

public interface MyCarListClient {

    @GET("/wunderbucket/locations.json")
    Call<MyCarList> fetchCarList();
}

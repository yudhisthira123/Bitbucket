package com.example.yudhisthira.wunder.model;

import com.example.yudhisthira.wunder.data.MyCarList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by yudhisthira on 12/05/17.
 *
 * This car list client interface by which we will request to Retrofit
 */
public interface MyCarListClient {

    /**
     *
     * @return
     *
     * GET request to Retrofit to fetch response for server
     */
    @GET("/wunderbucket/locations.json")
    Call<MyCarList> fetchCarList();
}

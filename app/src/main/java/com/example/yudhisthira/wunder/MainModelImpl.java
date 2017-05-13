package com.example.yudhisthira.wunder;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yudhisthira on 10/05/17.
 */

public class MainModelImpl implements IMainModel {

    private List<Car> mCarList = null;

    private  ICarsResponseCallback mCallback;

    @Override
    public void fetchCarsData(ICarsResponseCallback callback) {

        mCallback = callback;

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://s3-us-west-2.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MyCarListClient myCarListClient = retrofit.create(MyCarListClient.class);

        Call<MyCarList> call = myCarListClient.fetchCarList();

        call.enqueue(new Callback<MyCarList>() {
            @Override
            public void onResponse(Call<MyCarList> call, Response<MyCarList> response) {

                MyCarList list = response.body();

                mCarList = list.getCarList();
                mCallback.onSuccess(mCarList);

                Log.d("", "");
            }

            @Override
            public void onFailure(Call<MyCarList> call, Throwable t) {
                mCallback.onFailure();
                Log.d("", "");
            }
        });

    }

    @Override
    public List<Car> getStoredData() {
        return mCarList;
    }
}

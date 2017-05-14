package com.example.yudhisthira.wunder.model;

import android.util.Log;

import com.example.yudhisthira.wunder.data.Car;
import com.example.yudhisthira.wunder.data.MyCarList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This is implementation class of ICarListModel
 */
public class CarListModelImpl implements ICarListModel {

    /**
     * List of Car object
     */
    private List<Car> mCarList = null;

    /**
     * Car list response callback object
     */
    private  ICarsResponseCallback mCallback;

    /**
     * @param callback ICarsResponseCallback object where response will be notify
     */
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

            }

            @Override
            public void onFailure(Call<MyCarList> call, Throwable t) {
                mCallback.onFailure();
            }
        });

    }

    /**
     *
     * @return List of Car objects.
     *
     * This function will return stored List of Car
     */
    @Override
    public List<Car> getStoredData() {
        return mCarList;
    }
}

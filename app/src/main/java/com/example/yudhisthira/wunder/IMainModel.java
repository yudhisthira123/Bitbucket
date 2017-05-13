package com.example.yudhisthira.wunder;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 */

public interface IMainModel {

    public interface ICarsResponseCallback{
        public void onSuccess(List<Car> strings);
        public void onFailure();
    }

    public void fetchCarsData(ICarsResponseCallback callback);
    public List<Car> getStoredData();
}

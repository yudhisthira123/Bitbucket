package com.example.yudhisthira.wunder.presenter;

import com.example.yudhisthira.wunder.data.Car;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This Car list presenter interface
 */
public interface ICarListPresenter {
    /**
     * This function sends asynchronous request to backend to fetch car list
     */
    public void fetchCarsData();

    /**
     *
     * @return List of Car
     *
     * This function returns the already stored information
     */
    public List<Car> getStoredData();
}

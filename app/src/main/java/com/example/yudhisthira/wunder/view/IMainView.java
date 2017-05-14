package com.example.yudhisthira.wunder.view;

import com.example.yudhisthira.wunder.data.Car;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This is car list view interface. The UI component where we have to car list, has to implement this interface
 */
public interface IMainView {
    /**
     * Show progress wheel on UI while loading car list data
     */
    void showProgress();

    /**
     * @param carList List of Car object.
     *
     * Display car list on UI
     */
    void showCars(List<Car> carList);

    /**
     * Show error message if Any.
     */
    void showErrorMessage();
}

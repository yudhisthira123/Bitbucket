package com.example.yudhisthira.wunder.presenter;

import com.example.yudhisthira.wunder.data.Car;
import com.example.yudhisthira.wunder.model.ICarListModel;
import com.example.yudhisthira.wunder.view.IMainView;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This is implementation of ICarListPresenter
 */
public class CarListPresenterImpl implements ICarListPresenter, ICarListModel.ICarsResponseCallback {

    /**
     * IMainView instance
     */
    private IMainView mMainView;

    /**
     * ICarListModel instance
     */
    private ICarListModel mMainModel;

    /**
     * Constructor of this class
     * @param mainView IMainView instance
     * @param mainModel ICarListModel instance
     */
    public CarListPresenterImpl(IMainView mainView, ICarListModel mainModel) {
        mMainView = mainView;
        mMainModel = mainModel;
    }

    /**
     * This function sends asynchronous request to backend to fetch car list
     */
    @Override
    public void fetchCarsData() {
        mMainView.showProgress();

        mMainModel.fetchCarsData(this);
    }

    /**
     *
     * @return List of Car
     *
     * This function returns the already stored information
     */
    @Override
    public List<Car> getStoredData() {
        return mMainModel.getStoredData();
    }

    /**
     * @param carList List of car
     *
     * This function notify success response with list of car
     */
    @Override
    public void onSuccess(List<Car> carList) {
        mMainView.showCars(carList);
    }

    /**
     * This function notify failure response
     */
    @Override
    public void onFailure() {
        mMainView.showErrorMessage();
    }
}

package com.example.yudhisthira.wunder;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 */

public class MainPresenterImpl implements IMainPresenter, IMainModel.ICarsResponseCallback {

    private IMainView mMainView;
    private IMainModel mMainModel;

    public MainPresenterImpl(IMainView mainView, IMainModel mainModel) {
        mMainView = mainView;
        mMainModel = mainModel;
    }

    @Override
    public void fetchCarsData() {
        mMainView.showProgress();

        mMainModel.fetchCarsData(this);
    }

    @Override
    public List<Car> getStoredData() {
        return mMainModel.getStoredData();
    }

    @Override
    public void onButtonClick(int id) {
        mMainView.onButtonClick(id);
    }

    @Override
    public void onSuccess(List<Car> carList) {
        mMainView.showCars(carList);
    }

    @Override
    public void onFailure() {
        mMainView.showErrorMessage();
    }
}

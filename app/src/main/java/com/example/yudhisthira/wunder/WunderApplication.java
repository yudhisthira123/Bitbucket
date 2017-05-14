package com.example.yudhisthira.wunder;

import android.app.Application;

import com.example.yudhisthira.wunder.presenter.ICarListPresenter;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * Application class
 */
public class WunderApplication extends Application {
    private ICarListPresenter mMainPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setMainPresenterToApp(ICarListPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    public ICarListPresenter getMainPresenterFromApp() {
        return mMainPresenter;
    }
}

package com.example.yudhisthira.wunder;

import android.app.Application;

/**
 * Created by yudhisthira on 10/05/17.
 */

public class WunderApplication extends Application {
    private IMainPresenter                  mMainPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setMainPresenterToApp(IMainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
    }

    public IMainPresenter getMainPresenterFromApp() {
        return mMainPresenter;
    }
}

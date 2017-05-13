package com.example.yudhisthira.wunder;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 */

public interface IMainView {
    void showProgress();
    void showCars(List<Car> carList);
    void showErrorMessage();
    void onButtonClick(int id);
}

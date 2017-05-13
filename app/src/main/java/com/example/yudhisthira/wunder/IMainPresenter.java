package com.example.yudhisthira.wunder;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 */

public interface IMainPresenter {
    public void fetchCarsData();
    public List<Car> getStoredData();
    public void onButtonClick(int id);
}

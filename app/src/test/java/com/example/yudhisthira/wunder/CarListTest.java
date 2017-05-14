package com.example.yudhisthira.wunder;

import android.util.Log;

import com.example.yudhisthira.wunder.data.Car;
import com.example.yudhisthira.wunder.model.CarListModelImpl;
import com.example.yudhisthira.wunder.presenter.CarListPresenterImpl;
import com.example.yudhisthira.wunder.presenter.ICarListPresenter;
import com.example.yudhisthira.wunder.view.IMainView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by yudhisthira on 12/05/17.
 */

public class CarListTest {

    private List<Car> mCarList;

    private CountDownLatch mCountDownLatch = new CountDownLatch(1);


    public class MockClass implements IMainView {
        @Override
        public void showProgress() {
            Log.d("testListData", "showProgress");
        }

        @Override
        public void showCars(List<Car> carList) {
            Log.d("testListData", "showProgress");
            mCarList = carList;
            mCountDownLatch.countDown();
        }

        @Override
        public void showErrorMessage() {
            Log.d("testListData", "showProgress");
            mCountDownLatch.countDown();
        }
    }

    @Before
    public void setup() {

    }

    @Test
    public void carListResponseReceived() throws Exception{

        mCountDownLatch = new CountDownLatch(1);

        ICarListPresenter mainPresenter = new CarListPresenterImpl(new MockClass(), new CarListModelImpl());

        mainPresenter.fetchCarsData();

        mCountDownLatch.await(4000, TimeUnit.MILLISECONDS);

        assertNotNull(mCarList);
    }

    @Test
    public void carsAvailableInList() throws Exception {
        mCountDownLatch = new CountDownLatch(1);

        ICarListPresenter mainPresenter = new CarListPresenterImpl(new MockClass(), new CarListModelImpl());

        mainPresenter.fetchCarsData();

        mCountDownLatch.await(4000, TimeUnit.MILLISECONDS);

        assertTrue(mCarList.size() > 0);
    }

    @After
    public void tearDown() {

    }
}

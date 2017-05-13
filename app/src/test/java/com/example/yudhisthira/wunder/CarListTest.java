package com.example.yudhisthira.wunder;

import android.util.Log;

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

        @Override
        public void onButtonClick(int id) {

        }
    }

    @Before
    public void setup() {

    }

    @Test
    public void carListResponseReceived() throws Exception{

        mCountDownLatch = new CountDownLatch(1);

        IMainPresenter mainPresenter = new MainPresenterImpl(new MockClass(), new MainModelImpl());

        mainPresenter.fetchCarsData();

        mCountDownLatch.await(4000, TimeUnit.MILLISECONDS);

        assertNotNull(mCarList);
    }

    @Test
    public void carsAvailableInList() throws Exception {
        mCountDownLatch = new CountDownLatch(1);

        IMainPresenter mainPresenter = new MainPresenterImpl(new MockClass(), new MainModelImpl());

        mainPresenter.fetchCarsData();

        mCountDownLatch.await(4000, TimeUnit.MILLISECONDS);

        assertTrue(mCarList.size() > 0);
    }

    @After
    public void tearDown() {

    }
}

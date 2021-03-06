package com.example.yudhisthira.wunder;

import com.example.yudhisthira.wunder.model.MapModelImpl;
import com.example.yudhisthira.wunder.presenter.IMapPresenter;
import com.example.yudhisthira.wunder.presenter.MapPresenterImpl;
import com.example.yudhisthira.wunder.view.IMainMapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by yudhisthira on 12/05/17.
 */

public class CarMapTest {

    private CountDownLatch mCountDownLatch;
    private GoogleMap mGoogleMap;

    public class MapMockClass implements IMainMapView {
        @Override
        public void showMap(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            mCountDownLatch.countDown();
        }
    }

    @Test
    public void sampleTest() throws Exception{

        mCountDownLatch = new CountDownLatch(1);

        IMapPresenter mapPresenter = new MapPresenterImpl(new MapMockClass(), new MapModelImpl());

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();

        mapPresenter.fetchMap(supportMapFragment);

        mCountDownLatch.await(4000, TimeUnit.MILLISECONDS);

        assertNotNull(mGoogleMap);
    }

}

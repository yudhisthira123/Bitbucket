package com.example.yudhisthira.wunder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by yudhisthira on 10/05/17.
 */

public class MapPresenterImpl implements IMapPresenter, IMapModel.IMapResponseCallback {

    private IMapModel mMapModel;
    private IMainMapView mMainMapView;

    public MapPresenterImpl(IMainMapView mapView, IMapModel mapModel) {
        mMainMapView = mapView;
        mMapModel = mapModel;
    }

    @Override
    public void fetchMap(MapView mapView) {
        mMapModel.fetchCarsData(mapView, this);
    }

    @Override
    public void fetchMap1(SupportMapFragment supportMapFragment) {
        mMapModel.fetchCarsData1(supportMapFragment, this);
    }

    @Override
    public GoogleMap getStoredMap() {
        return mMapModel.getStoredData();
    }

    @Override
    public void onSuccess(GoogleMap googleMap) {
        if(null != mMainMapView) {
            mMainMapView.showMap(googleMap);
        }
    }

    @Override
    public void onFailure() {

    }
}

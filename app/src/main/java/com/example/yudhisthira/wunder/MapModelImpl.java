package com.example.yudhisthira.wunder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by yudhisthira on 10/05/17.
 */

public class MapModelImpl implements IMapModel, OnMapReadyCallback {

    private IMapResponseCallback mMapResponseCallback;
    private GoogleMap mGoogleMap;

    @Override
    public void fetchCarsData(MapView mapView, IMapResponseCallback callback) {
        mMapResponseCallback = callback;

        mapView.getMapAsync(this);
    }

    @Override
    public void fetchCarsData1(SupportMapFragment supportMapFragment, IMapResponseCallback callback) {
        mMapResponseCallback = callback;

        supportMapFragment.getMapAsync(this);
    }

    @Override
    public GoogleMap getStoredData() {
        return mGoogleMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(null != mMapResponseCallback) {
            mMapResponseCallback.onSuccess(googleMap);
        }
    }
}

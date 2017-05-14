package com.example.yudhisthira.wunder.model;

import com.example.yudhisthira.wunder.model.IMapModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * Thsi implementaion class of IMapModel
 */
public class MapModelImpl implements IMapModel, OnMapReadyCallback {

    /**
     * Map response callback object
     */
    private IMapResponseCallback mMapResponseCallback;

    /**
     * GoogleMap object
     */
    private GoogleMap mGoogleMap;

    /**
     *
     * @param supportMapFragment instance of SupportMapFragment
     * @param callback instance IMapResponseCallback for callback notification
     */
    @Override
    public void fetchCarsData(SupportMapFragment supportMapFragment, IMapResponseCallback callback) {
        mMapResponseCallback = callback;

        supportMapFragment.getMapAsync(this);
    }

    /**
     *
     * @return GoogleMap
     *
     * This function return stored GoogleMap
     */
    @Override
    public GoogleMap getStoredData() {
        return mGoogleMap;
    }

    /**
     * @param googleMap GoogleMap
     *
     * Called when Google map is ready
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(null != mMapResponseCallback) {
            mMapResponseCallback.onSuccess(googleMap);
        }
    }
}

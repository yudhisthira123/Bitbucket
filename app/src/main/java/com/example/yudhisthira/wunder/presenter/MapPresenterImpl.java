package com.example.yudhisthira.wunder.presenter;

import com.example.yudhisthira.wunder.model.IMapModel;
import com.example.yudhisthira.wunder.view.IMainMapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This is Map presenter implementation class
 */
public class MapPresenterImpl implements IMapPresenter, IMapModel.IMapResponseCallback {

    /**
     * IMapModel instance
     */
    private IMapModel mMapModel;

    /**
     * IMainMapView instance
     */
    private IMainMapView mMainMapView;

    /**
     * Constructor of this class
     * @param mapView IMainMapView instance
     * @param mapModel IMapModel instance
     */
    public MapPresenterImpl(IMainMapView mapView, IMapModel mapModel) {
        mMainMapView = mapView;
        mMapModel = mapModel;
    }

    /**
     *
     * @param supportMapFragment SupportMapFragment instance
     *
     * This function send asynchronous request to fetch GoogleMap
     */
    @Override
    public void fetchMap(SupportMapFragment supportMapFragment) {
        mMapModel.fetchCarsData(supportMapFragment, this);
    }

    /**
     *
     * @return GoogleMap object
     *
     * This function return stored GoogleMap
     */
    @Override
    public GoogleMap getStoredMap() {
        return mMapModel.getStoredData();
    }

    /**
     *
     * @param googleMap
     *
     * This function will notify success response to caller
     */
    @Override
    public void onSuccess(GoogleMap googleMap) {
        if(null != mMainMapView) {
            mMainMapView.showMap(googleMap);
        }
    }

    /**
     * This function will notify failure response to caller
     */
    @Override
    public void onFailure() {

    }
}

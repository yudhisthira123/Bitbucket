package com.example.yudhisthira.wunder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 */

public interface IMapModel {
    public interface IMapResponseCallback{
        public void onSuccess(GoogleMap googleMap);
        public void onFailure();
    }

    public void fetchCarsData(MapView mapView, IMapResponseCallback callback);
    public void fetchCarsData1(SupportMapFragment supportMapFragment, IMapResponseCallback callback);
    public GoogleMap getStoredData();
}

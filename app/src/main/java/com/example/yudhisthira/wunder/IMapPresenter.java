package com.example.yudhisthira.wunder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by yudhisthira on 10/05/17.
 */

public interface IMapPresenter {
    public void fetchMap(MapView mapView);
    public void fetchMap1(SupportMapFragment supportMapFragment);
    public GoogleMap getStoredMap();
}

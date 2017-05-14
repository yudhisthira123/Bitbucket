package com.example.yudhisthira.wunder.presenter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by yudhisthira on 10/05/17.
 * This Map presenter interface
 */
public interface IMapPresenter {
    /**
     *
     * @param supportMapFragment SupportMapFragment instance
     *
     * This function send asynchronous request to fetch GoogleMap
     */
    public void fetchMap(SupportMapFragment supportMapFragment);

    /**
     *
     * @return GoogleMap object
     *
     * This function return stored GoogleMap
     */
    public GoogleMap getStoredMap();
}

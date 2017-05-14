package com.example.yudhisthira.wunder.view;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This is map view interface. The UI component where we have to show map, has to implement this interface
 */
public interface IMainMapView {
    /**
     * @param googleMap GoogleMap which will be shown on UI
     */
    public void showMap(GoogleMap googleMap);
}

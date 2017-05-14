package com.example.yudhisthira.wunder.model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

/**
 * Created by yudhisthira on 10/05/17.
 *
 * This is map model interface
 */
public interface IMapModel {

    /**
     * This is callback interface. It has to implement by caller.
     *
     * Succes and failure esponse will be notify here.
     */
    public interface IMapResponseCallback{
        /**
         *
         * @param googleMap
         *
         * This function will notify success response to caller
         */
        public void onSuccess(GoogleMap googleMap);

        /**
         * This function will notify failure response to caller
         */
        public void onFailure();
    }

    /**
     *
     * @param supportMapFragment instance of SupportMapFragment
     * @param callback instance IMapResponseCallback for callback notification
     */
    public void fetchCarsData(SupportMapFragment supportMapFragment, IMapResponseCallback callback);

    /**
     *
     * @return GoogleMap
     *
     * This function return stored GoogleMap
     */
    public GoogleMap getStoredData();
}

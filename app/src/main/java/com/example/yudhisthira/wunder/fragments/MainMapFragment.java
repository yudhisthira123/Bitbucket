package com.example.yudhisthira.wunder.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yudhisthira.wunder.CommonConstants;
import com.example.yudhisthira.wunder.FetchAddressIntentService;
import com.example.yudhisthira.wunder.R;
import com.example.yudhisthira.wunder.data.Car;
import com.example.yudhisthira.wunder.model.MapModelImpl;
import com.example.yudhisthira.wunder.presenter.IMapPresenter;
import com.example.yudhisthira.wunder.presenter.MapPresenterImpl;
import com.example.yudhisthira.wunder.view.IMainMapView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudhisthira on 11/05/17.
 *
 * This Fragment sub class for showing Googele Map
 */
public class MainMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener,
        IMainMapView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private static final String             TAG = MainMapFragment.class.getSimpleName();

    /**
     * GoogleMap instance
     */
    private GoogleMap                       mGoogleMap;

    /**
     * List of Car
     */
    private List<Car>                       mCarList;

    /**
     * List of Marker
     */
    private List<Marker>                    mMarkerList;

    /**
     * List of MarkerOptions
     */
    private List<MarkerOptions>             mMarkerOptionsList;

    /**
     * Boolean flag to show all markers or not
     */
    private boolean                         mShowAll = true;

    /**
     * Map presenter
     */
    private IMapPresenter mMapPresenter;

    /**
     *  SupportMapFragment object
     */
    private SupportMapFragment              mSupportMapFragment;

    /**
     *  GoogleApiClient object
     */
    private GoogleApiClient                 mGoogleApiClient;

    /**
     * LocationRequest request
     */
    private LocationRequest                 mLocationRequest;

    /**
     *  BroadcastReceiver receiver
     */
    private BroadcastReceiver               mBroadcastReceiver;

    /**
     * boolean
     */
    private boolean                         mbPermissionBroadcastReceived = false;

    /**
     * AddressResultReceiver result receiver
     */
    private AddressResultReceiver           mResultReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            checkLocationPermission();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        long start = System.currentTimeMillis();

        View v = inflater.inflate(R.layout.map_view_frag, container, false);

        Bundle b = getArguments();
        //mCarList = (List<Car>) b.getSerializable("CAR_LIST");

        long end = System.currentTimeMillis();
        long time = end - start;
        Log.d("PROFILING" + " " + TAG, "onCreateView time = " + time);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        long start = System.currentTimeMillis();

        FragmentManager fm = getChildFragmentManager();

        mSupportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mSupportMapFragment == null) {
            mSupportMapFragment = SupportMapFragment.newInstance();
            mSupportMapFragment.setRetainInstance(true);

            mMapPresenter = new MapPresenterImpl(this, new MapModelImpl());
//            mMapPresenter.fetchMap1(mSupportMapFragment);

            fm.beginTransaction().replace(R.id.map_container, mSupportMapFragment).commit();
        }
        else {

        }

        long end = System.currentTimeMillis();
        long time = end - start;
        Log.d("PROFILING" + " " + TAG, "onActivityCreated = " + time);
    }

    @Override
    public void onStart() {
        super.onStart();

        registerBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();

        unRegisterBroadcastReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != mGoogleApiClient) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     *
     * @param requestCode int for unique permission request
     * @param permissions List of requested permission
     * @param grantResults int list of grant result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CommonConstants.MY_LOCATION_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }

                        if(null != mGoogleMap) {
                            mGoogleMap.setMyLocationEnabled(true);
                        }
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case CommonConstants.REQUEST_CHECK_SETTINGS:
                Log.d("", "");
                break;

        }
    }

    /**
     * This is Google API callback. this is called when connection with google API is success
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Google API onConnected");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        Log.d("", "");
                        break;

                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        Log.d("", "");
                        try {
                            status.startResolutionForResult(getActivity(), CommonConstants.REQUEST_CHECK_SETTINGS);
                        }
                        catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d("", "");
                        break;
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * This function is called when connection is suspended
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Google API onConnectionSuspended");
    }

    /**
     * This GoogleApi callback. this called called when GoogleApiConnection falied
     * @param connectionResult ConnectionResult object
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Google API onConnectionFailed");
    }

    /**
     * This function called when device location changed or first time detected
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Google API onLocationChanged");

        startIntentService(location);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * This function which display GoogleMap on UI
     * @param googleMap GoogleMap which will be shown on UI
     */
    @Override
    public void showMap(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        UiSettings uiSettings = mGoogleMap.getUiSettings();

        mGoogleMap.setOnMarkerClickListener(this);

        createMarkerList();

        if(null != mMarkerList && mMarkerList.size() > 0) {
            final LatLngBounds.Builder builder = LatLngBounds.builder();

            for (Marker marker : mMarkerList) {
                builder.include(marker.getPosition());
            }

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
        }

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                if(null == mGoogleApiClient) {
                    buildGoogleApiClient();
                }
                if(null != mGoogleMap) {
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
            else {
                if(true == mbPermissionBroadcastReceived) {
                    checkLocationPermission();
                    mbPermissionBroadcastReceived = false;
                }
            }
        }
        else {
            buildGoogleApiClient();
            if(null != mGoogleMap) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }

    /**
     *
     * @param marker Marker object
     * @return boolean, true if handled else false
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        boolean bConsume = false;

        if(true == mShowAll) {
            mShowAll = false;
            hideAllMarkerExceptThis(marker);
        }
        else {
            mShowAll = true;
            showAllMarkers();
            marker.hideInfoWindow();

            bConsume = true;

        }

        return bConsume;
    }

    /**
     * This function hide all markers excepts given one
     * @param marker Marker object
     */
    private void hideAllMarkerExceptThis(Marker marker) {

        for(Marker marker1 : mMarkerList) {
            marker1.setVisible(false);
        }

        marker.setVisible(true);
    }

    /**
     * This function shows all markers on GoogleMap
     */
    private void showAllMarkers() {

        for(Marker marker1 : mMarkerList) {
            marker1.setVisible(true);
        }

        if(null != mMarkerList && mMarkerList.size() > 0) {
            final LatLngBounds.Builder builder = LatLngBounds.builder();

            for (Marker marker : mMarkerList) {
                builder.include(marker.getPosition());
            }

            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
        }
    }

    /**
     * This function create the list of markers of car which need to display on GoogleMap
     */
    private void createMarkerList() {
        mMarkerList = new ArrayList<>();

        if(null != mCarList && mCarList.size() > 0) {
            mMarkerOptionsList = new ArrayList<>(mCarList.size());

            for (Car car : mCarList) {

                LatLng latLng = new LatLng(car.getLatitude(), car.getLongitude());

                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(car.getCarName()).snippet(car.getCarAddress());
                mMarkerOptionsList.add(markerOptions);

                Marker marker = mGoogleMap.addMarker(markerOptions);
                mMarkerList.add(marker);
            }
        }
    }

    /**
     * This function create GoogleApClient and sent connect request
     */
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    /**
     * This function check for ACCESS_FINE_LOCATION permission
     *
     * @return true if location permission are granted else false
     *
     */
    private boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        CommonConstants.MY_LOCATION_PERMISSIONS);

            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        CommonConstants.MY_LOCATION_PERMISSIONS);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * This function register broadcast receiver for LOCATION_PERMISSION_RESULT_ALERT_CLOSE
     * and CAR_LIST_AVALABLE
     */
    private void registerBroadcastReceiver() {

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String strAction = intent.getAction();

                if(CommonConstants.LOCATION_PERMISSION_RESULT_ALERT_CLOSE.equals(strAction)) {

                    boolean granted = intent.getBooleanExtra(CommonConstants.LOCATION_PERMISSION_RESULT, false);
                    if(true == granted) {
                        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            checkLocationPermission();
                        }

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }

                        if(null != mGoogleMap) {
                            mGoogleMap.setMyLocationEnabled(true);
                        }

                        mbPermissionBroadcastReceived = true;
                    }
                }
                else if(CommonConstants.CAR_LIST_AVALABLE.equals(strAction)) {
                    Bundle b = intent.getBundleExtra(CommonConstants.CAR_LIST_BUNDLE);
                    if(null != b) {
                        mCarList = (List<Car>) b.getSerializable(CommonConstants.CAR_LIST);
                    }

                    mMapPresenter.fetchMap(mSupportMapFragment);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(CommonConstants.CAR_LIST_AVALABLE);
        intentFilter.addAction(CommonConstants.LOCATION_PERMISSION_RESULT_ALERT_CLOSE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    /**
     * Unregister broadcast receiver
     */
    private void unRegisterBroadcastReceiver() {
        if(null != mBroadcastReceiver) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }

    /**
     *
     * @param resultCode 0 for success else failure
     * @param location Location for which address required
     * @param address String address of given location coordinates
     */
    private void focusToLocation(int resultCode, Location location, String address) {

        final LatLngBounds.Builder builder = LatLngBounds.builder();

        if(null != mMarkerList) {
            for (Marker marker : mMarkerList) {
                builder.include(marker.getPosition());
            }

            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            builder.include(myLocation);

            Marker marker;
            if(resultCode == CommonConstants.SUCCESS_RESULT) {
                marker = mGoogleMap.addMarker(new MarkerOptions().position(myLocation).title("Device Location").snippet(address).icon(BitmapDescriptorFactory.fromResource(R.drawable.map)));
            }
            else {
                marker = mGoogleMap.addMarker(new MarkerOptions().position(myLocation).title("Device Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.map)));
            }

            mMarkerList.add(marker);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
        }

        View v = getActivity().findViewById(R.id.map_container);

        Snackbar.make(v, "Current location detected", Snackbar.LENGTH_LONG).show();
    }

    /**
     *
     * @param location Location for which we need to get Geographic location address
     */
    private void startIntentService(Location location) {
        Intent intent = new Intent(getContext(), FetchAddressIntentService.class);

        mResultReceiver = new AddressResultReceiver(new Handler());

        intent.putExtra(CommonConstants.RECEIVER, mResultReceiver);
        intent.putExtra(CommonConstants.LOCATION_DATA_EXTRA, location);
        getContext().startService(intent);
    }

    /**
     * Class which will receive Geographic location address from service
     */
    public class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String address = resultData.getString(CommonConstants.RESULT_DATA_KEY);
            Location location= resultData.getParcelable(CommonConstants.LOCATION_DATA_EXTRA);

            focusToLocation(resultCode, location, address);

            super.onReceiveResult(resultCode, resultData);
        }
    }
}

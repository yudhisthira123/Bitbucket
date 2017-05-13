package com.example.yudhisthira.wunder;

import android.*;
import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudhisthira on 11/05/17.
 */

public class MainMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener,
        IMainMapView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private static final String             TAG = MainMapFragment.class.getSimpleName();
    private static final int                MY_LOCATION_PERMISSIONS = 1000;
    private static final int                REQUEST_CHECK_SETTINGS = 1001;

    private GoogleMap                       mGoogleMap;
    private List<Car>                       mCarList;
    private List<Marker>                    mMarkerList;
    private List<MarkerOptions>             mMarkerOptionsList;
    private boolean                         mShowAll = true;
    private IMapPresenter                   mMapPresenter;

    private SupportMapFragment              mSupportMapFragment;

    private GoogleApiClient                 mGoogleApiClient;
    LocationRequest                         mLocationRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.map_view_frag, container, false);

        Bundle b = getArguments();
        mCarList = (List<Car>) b.getSerializable("CAR_LIST");

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();

        mSupportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mSupportMapFragment == null) {
            mSupportMapFragment = SupportMapFragment.newInstance();
            mSupportMapFragment.setRetainInstance(true);

            mMapPresenter = new MapPresenterImpl(this, new MapModelImpl());
            mMapPresenter.fetchMap1(mSupportMapFragment);

            fm.beginTransaction().replace(R.id.map_container, mSupportMapFragment).commit();
        }
        else {

        }

//        mMapPresenter = new MapPresenterImpl(this, new MapModelImpl());
//        mMapPresenter.fetchMap1(mSupportMapFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != mGoogleApiClient) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_PERMISSIONS: {
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

            case REQUEST_CHECK_SETTINGS:
                Log.d("", "");
                break;

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }

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
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
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

//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Google API onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Google API onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Google API onLocationChanged");

        final LatLngBounds.Builder builder = LatLngBounds.builder();

        if(null != mMarkerList && mMarkerList.size() > 0) {
            for (Marker marker : mMarkerList) {
                builder.include(marker.getPosition());
            }

            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            builder.include(myLocation);

            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(myLocation).title("MyLocation").icon(BitmapDescriptorFactory.fromResource(R.drawable.map)));
            mMarkerList.add(marker);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
        }



        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void showMap(GoogleMap googleMap) {
        mGoogleMap = googleMap;
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

                buildGoogleApiClient();
                if(null != mGoogleMap) {
                    mGoogleMap.setMyLocationEnabled(true);
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

    private void hideAllMarkerExceptThis(Marker marker) {

        for(Marker marker1 : mMarkerList) {
            marker1.setVisible(false);
        }

        marker.setVisible(true);
    }

    private void showAllMarkers() {

        for(Marker marker1 : mMarkerList) {
            marker1.setVisible(true);
        }

        if(null != mMarkerList && mMarkerList.size() > 0) {
            final LatLngBounds.Builder builder = LatLngBounds.builder();

            for (Marker marker : mMarkerList) {
                builder.include(marker.getPosition());
            }

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 50));
        }
    }

    private void createMarkerList() {

        if(null != mCarList && mCarList.size() > 0) {

            mMarkerList = new ArrayList<>(mCarList.size());
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

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_PERMISSIONS);

            } else {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_PERMISSIONS);
            }
            return false;
        } else {
            return true;
        }
    }
}

package com.example.yudhisthira.wunder;

import android.*;
import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudhisthira on 11/05/17.
 */

public class CarListFragment extends Fragment implements IMainView, View.OnClickListener{

    private static final String             TAG = CarListFragment.class.getSimpleName();

    private static final int                MY_LOCATION_PERMISSIONS = 1000;
    private static final int                REQUEST_CHECK_SETTINGS = 1001;

    private RecyclerView                    mRecyclerView;
    private RecyclerAdapter                 mRecyclerAdapter;
    private RecyclerView.LayoutManager      mLayoutManager;

    private IMainPresenter                  mMainPresenter;

    private Button                          mMapButton;
    private ProgressBar                     mProgressBar;
    private TextView                        mErrorTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.list_view_fagment, container,false);

        mMapButton = (Button)v.findViewById(R.id.btnMapView);
        mMapButton.setOnClickListener(this);
        mProgressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        mErrorTextView = (TextView)v.findViewById(R.id.errorTextView);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);


        if(null == savedInstanceState) {
            mMainPresenter = new MainPresenterImpl(this, new MainModelImpl());
            setMainPresenterToApp(mMainPresenter);

            mMainPresenter.fetchCarsData();
        }
        else {
            mMainPresenter = getMainPresenterFromApp();
            List<Car> list = mMainPresenter.getStoredData();

            if(null == list || list.size() < 1) {
                mMainPresenter.fetchCarsData();
            }

            mRecyclerAdapter.setData(list);
            if(null != mMapButton) {
                mMapButton.setEnabled(true);
            }
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //mMainPresenter.fetchCarsData();
    }

    @Override
    public void onClick(View v) {
        mMainPresenter.onButtonClick(v.getId());
    }

    @Override
    public void showProgress() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        if(null != mMapButton) {
            mMapButton.setEnabled(false);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setVisibility(View.GONE);
        }
    }

    public void hideProgress() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }

        if(null != mMapButton) {
            mMapButton.setEnabled(true);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCars(List<Car> carList) {
        mRecyclerAdapter.setData(carList);

//        if(null != mErrorTextView) {
//            mErrorTextView.setVisibility(View.GONE);
//        }

        hideProgress();
    }

    @Override
    public void showErrorMessage() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }

        if(null != mMapButton) {
            mMapButton.setEnabled(false);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setText("Fail to load Data!!!");
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onButtonClick(int id) {
        if(id == R.id.btnMapView) {

            FragmentManager fm = getFragmentManager();
            MainMapFragment mainMapFragment = (MainMapFragment)fm.findFragmentByTag("MAP_FRAGMENT");


            if(null == mainMapFragment) {
                mainMapFragment = new MainMapFragment();

                Bundle b = new Bundle();
                b.putSerializable("CAR_LIST", (ArrayList<Car>)mMainPresenter.getStoredData());
                mainMapFragment.setArguments(b);

                mainMapFragment.setRetainInstance(true);
            }
            FragmentTransaction transaction = fm.beginTransaction();

            transaction.add(R.id.main_container, mainMapFragment, "MAP_FRAGMENT");
            transaction.addToBackStack("MAP_FRAGMENT1");
            transaction.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case REQUEST_CHECK_SETTINGS:
                Log.d("", "");
                break;
        }
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

    private void setMainPresenterToApp(IMainPresenter mainPresenter) {

        WunderApplication application = (WunderApplication) getActivity().getApplication();

        application.setMainPresenterToApp(mainPresenter);
    }

    private IMainPresenter getMainPresenterFromApp() {
        WunderApplication application = (WunderApplication) getActivity().getApplication();

        return application.getMainPresenterFromApp();
    }
}

package com.example.yudhisthira.wunder.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudhisthira.wunder.CommonConstants;
import com.example.yudhisthira.wunder.R;
import com.example.yudhisthira.wunder.WunderApplication;
import com.example.yudhisthira.wunder.data.Car;
import com.example.yudhisthira.wunder.model.CarListModelImpl;
import com.example.yudhisthira.wunder.presenter.CarListPresenterImpl;
import com.example.yudhisthira.wunder.presenter.ICarListPresenter;
import com.example.yudhisthira.wunder.view.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudhisthira on 11/05/17.
 *
 * This is a Fragment class which shows Cars information in recycler view as a list of items
 */
public class CarListFragment extends Fragment implements IMainView{

    private static final String             TAG = CarListFragment.class.getSimpleName();

    /**
     * Reference of Recycler view
     */
    private RecyclerView                    mRecyclerView;

    /**
     * Reference of Recycler adapter
     */
    private RecyclerAdapter                 mRecyclerAdapter;

    /**
     * Layout manager for recycler view
     */
    private RecyclerView.LayoutManager      mLayoutManager;

    /**
     * List of Car object
     */
    private List<Car>                       mCarList;

    /**
     * Car list presenter interface
     */
    private ICarListPresenter               mMainPresenter;

    /**
     * reference to ProgressBar
     */
    private ProgressBar                     mProgressBar;

    /**
     *  Reference to error text view
     */
    private TextView                        mErrorTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long start = System.currentTimeMillis();

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission();
        }

        mCarList = new ArrayList<>();

        long end = System.currentTimeMillis();
        long time = end - start;
        Log.d("PROFILING" + " " + TAG, "onCreate time = " + time);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        long start = System.currentTimeMillis();

        View v = inflater.inflate(R.layout.list_view_fagment, container,false);

        mProgressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        mErrorTextView = (TextView)v.findViewById(R.id.errorTextView);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerAdapter = new RecyclerAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);


        if(null == savedInstanceState) {
            mMainPresenter = new CarListPresenterImpl(this, new CarListModelImpl());
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
        }

        long end = System.currentTimeMillis();
        long time = end - start;
        Log.d("PROFILING" + " " + TAG, "onCreateView time = " + time);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * This function shows progress wheel, this called by presenter
     */
    @Override
    public void showProgress() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setVisibility(View.GONE);
        }
    }

    /**
     * This function hide progress wheel
     */
    private void hideProgress() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @param carList List of Car object.
     * Thsi function display the car list on UI, and this called by presenter
     */
    @Override
    public void showCars(List<Car> carList) {
        mCarList = carList;
        mRecyclerAdapter.setData(carList);

        Intent intent = new Intent(CommonConstants.CAR_LIST_AVALABLE);

        Bundle b = new Bundle();
        b.putSerializable(CommonConstants.CAR_LIST, (ArrayList<Car>)mCarList);
        intent.putExtra(CommonConstants.CAR_LIST_BUNDLE, b);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        hideProgress();
    }

    /**
     * This function display error message if list loading fail for any reason
     */
    @Override
    public void showErrorMessage() {
        if(null != mProgressBar) {
            mProgressBar.setVisibility(View.GONE);
        }

        if(null != mErrorTextView) {
            mErrorTextView.setText("Fail to load Data!!!");
            mErrorTextView.setVisibility(View.VISIBLE);
        }

        Intent intent = new Intent(CommonConstants.CAR_LIST_AVALABLE);
        Bundle b = new Bundle();

        b.putSerializable(CommonConstants.CAR_LIST, (ArrayList<Car>)mCarList);
        intent.putExtra(CommonConstants.CAR_LIST_BUNDLE, b);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    /**
     *
     * @param requestCode unique int for request code for requested permision
     * @param permissions list of permission requested
     * @param grantResults list of permission request result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CommonConstants.MY_LOCATION_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        checkLocationPermission();

                        sendLocationPermissionResult(true);
                    }
                }
                else {
                    Toast.makeText(getActivity(), "permission denied can not update current location", Toast.LENGTH_LONG).show();
                    sendLocationPermissionResult(false);
                }
                break;
            }

            case CommonConstants.REQUEST_CHECK_SETTINGS:
                Log.d("", "");
                break;
        }
    }

    /**
     *
     * @return boolean.true if permission is granted else false
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
     *
     * @param mainPresenter ICarListPresenter
     *
     * This function set the ICarListPresenter to Application class.
     */
    private void setMainPresenterToApp(ICarListPresenter mainPresenter) {

        WunderApplication application = (WunderApplication) getActivity().getApplication();

        application.setMainPresenterToApp(mainPresenter);
    }

    /**
     *
     * @return ICarListPresenter
     *
     * This function retuns ICarListPresenter from Application class.
     */
    private ICarListPresenter getMainPresenterFromApp() {
        WunderApplication application = (WunderApplication) getActivity().getApplication();

        return application.getMainPresenterFromApp();
    }

    /**
     *
     * @param granted boolean
     *
     * This function sends a local broadcast after location permission alert dialog dismissed
     */
    private void sendLocationPermissionResult(boolean granted) {

        Intent intent = new Intent(CommonConstants.LOCATION_PERMISSION_RESULT_ALERT_CLOSE);

        intent.putExtra(CommonConstants.LOCATION_PERMISSION_RESULT, granted);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}

package com.example.yudhisthira.wunder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.yudhisthira.wunder.fragments.CarListFragment;
import com.example.yudhisthira.wunder.fragments.MainMapFragment;

/**
 * Main Activity of application
 */
public class MainActivity extends AppCompatActivity {

    private static final String             TAG = MainActivity.class.getSimpleName();

    private Bundle                          mSavedInstanceState;

    /**
     * Handle of UI thread
     */
    private Handler                         mHandler;

    /**
     * Receiver to listen for ConnectivityManager.CONNECTIVITY_ACTION
     */
    private BroadcastReceiver               mNetworkReceiver;

    private boolean                         mIsFirstTime = true;

    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    handleNetworkResponse((boolean)msg.obj);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long start = System.currentTimeMillis();

        mSavedInstanceState = savedInstanceState;

        setContentView(R.layout.activity_main);

        mHandler = new MyHandler();

        checkForInternetConnection();

        long end = System.currentTimeMillis();
        long time = end - start;
        Log.d("PROFILING" + " " + TAG, "onCreate time = " + time);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterNetworkReceiver();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     *
     * @param bNetworkAvailable
     *
     * This function handles the response of internet connectivity
     */
    private void handleNetworkResponse(boolean bNetworkAvailable) {

        if(true == bNetworkAvailable) {

            addFragments(mSavedInstanceState);
        }
        else {
            displayNetworkErrorMessage();

            if(true == mIsFirstTime) {
                registerNetworkReceiver();
                mIsFirstTime = false;
            }
        }
    }

    /**
     *
     * @param savedInstanceState
     */
    private void addFragments(Bundle savedInstanceState) {

        if(null == savedInstanceState) {
            FragmentManager fm = getSupportFragmentManager();
            CarListFragment listFragment = (CarListFragment) fm.findFragmentByTag("LIST_FRAGMENT");
            if (null == listFragment) {
                listFragment = new CarListFragment();
                listFragment.setRetainInstance(true);
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.list_container, listFragment, "LIST_FRAGMENT");

            MainMapFragment mainMapFragment = (MainMapFragment)fm.findFragmentByTag("MAP_FRAGMENT");
            if(null == mainMapFragment) {
                mainMapFragment = new MainMapFragment();
            }

            mainMapFragment.setRetainInstance(true);
            transaction.replace(R.id.map_container, mainMapFragment, "MAP_FRAGMENT");
            transaction.commit();
        }
        else{

        }

        displayFragmentContainers();
    }

    /**
     *
     */
    private void displayFragmentContainers() {
        findViewById(R.id.networkCheckLayout).setVisibility(View.GONE);
        findViewById(R.id.list_container_lyt).setVisibility(View.VISIBLE);
        findViewById(R.id.map_container_lyt).setVisibility(View.VISIBLE);
        findViewById(R.id.networkErrorText).setVisibility(View.GONE);
    }

    /**
     *
     */
    private void displayNetworkErrorMessage() {
        findViewById(R.id.networkCheckLayout).setVisibility(View.GONE);
        findViewById(R.id.networkErrorText).setVisibility(View.VISIBLE);
    }

    /**
     * This function starts internet connectivity check
     */
    private void checkForInternetConnection(){
        findViewById(R.id.networkErrorText).setVisibility(View.GONE);
        findViewById(R.id.networkCheckLayout).setVisibility(View.VISIBLE);

        CheckForInternetConnection checkForInternetConnection = new CheckForInternetConnection(mHandler);
        checkForInternetConnection.start();
    }

    /**
     * This function register for ConnectivityManager.CONNECTIVITY_ACTION for network connectivity change
     */
    private void registerNetworkReceiver() {
        mNetworkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String strAction = intent.getAction();

                if(ConnectivityManager.CONNECTIVITY_ACTION.equals(strAction)) {
                    checkForInternetConnection();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkReceiver, intentFilter);
    }

    /**
     * unregister the network receiver
     */
    private void unregisterNetworkReceiver() {

        if (null != mNetworkReceiver) {
            unregisterReceiver(mNetworkReceiver);

            mNetworkReceiver = null;
        }
    }
}

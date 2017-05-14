package com.example.yudhisthira.wunder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yudhisthira.wunder.fragments.CarListFragment;
import com.example.yudhisthira.wunder.fragments.MainMapFragment;

/**
 * Main Activity of application
 */
public class MainActivity extends AppCompatActivity {

    private static final String             TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long start = System.currentTimeMillis();

        setContentView(R.layout.activity_main);

        if(null == savedInstanceState) {
            FragmentManager fm = getSupportFragmentManager();
            CarListFragment listFragment = (CarListFragment) fm.findFragmentByTag("LIST_FRAGMENT");
            if (null == listFragment) {
                listFragment = new CarListFragment();
                listFragment.setRetainInstance(true);
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.list_container, listFragment, "LIST_FRAGMENT");
            //transaction.commit();


            MainMapFragment mainMapFragment = (MainMapFragment)fm.findFragmentByTag("MAP_FRAGMENT");
            if(null == mainMapFragment) {
                mainMapFragment = new MainMapFragment();
            }

            mainMapFragment.setRetainInstance(true);
            //Bundle b = new Bundle();
            //b.putSerializable("CAR_LIST", (ArrayList<Car>)mMainPresenter.getStoredData());
            //mainMapFragment.setArguments(b);

            transaction.replace(R.id.map_container, mainMapFragment, "MAP_FRAGMENT");
            //transaction.addToBackStack("MAP_FRAGMENT1");
            transaction.commit();
        }
        else{

        }

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

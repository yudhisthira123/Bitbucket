package com.example.yudhisthira.wunder;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(null == savedInstanceState) {
            FragmentManager fm = getSupportFragmentManager();
            CarListFragment listFragment = (CarListFragment) fm.findFragmentByTag("LIST_FRAGMENT");
            if (null == listFragment) {
                listFragment = new CarListFragment();
                listFragment.setRetainInstance(true);
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, listFragment, "LIST_FRAGMENT");
            transaction.commit();
        }
        else{

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

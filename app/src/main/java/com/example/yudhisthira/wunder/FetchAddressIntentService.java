package com.example.yudhisthira.wunder;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by yudhisthira on 14/05/17.
 *
 * This is Intent servie which is used to geographic location of given locaion(lattitude and longitude)
 */
public class FetchAddressIntentService extends IntentService {

    private static final String TAG = FetchAddressIntentService.class.getSimpleName();

    private ResultReceiver mReceiver;

    public FetchAddressIntentService(){
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mReceiver = intent.getParcelableExtra(CommonConstants.RECEIVER);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        Location location = intent.getParcelableExtra(CommonConstants.LOCATION_DATA_EXTRA);

        List<Address> addresses = null;

        try {
            //Just get a single address.
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
        }

        if(null == addresses || addresses.size() == 0) {
            deliverResultToReceiver(CommonConstants.FAILURE_RESULT, "Fail to get address", location);
        }
        else {
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append(addresses.get(0).getAddressLine(0));
            strBuffer.append(" ,");
            strBuffer.append(addresses.get(0).getPostalCode());
            strBuffer.append(" ");
            strBuffer.append(addresses.get(0).getLocality());
            deliverResultToReceiver(CommonConstants.SUCCESS_RESULT, strBuffer.toString(), location);
        }
    }

    /**
     *
     * @param resultCode success of failure. 0 for success else fail
     * @param strAddress Address of location
     * @param location Location
     */
    private void deliverResultToReceiver(int resultCode, String strAddress, Location location) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConstants.RESULT_DATA_KEY, strAddress);
        bundle.putParcelable(CommonConstants.LOCATION_DATA_EXTRA, location);
        mReceiver.send(resultCode, bundle);
    }
}

package com.example.yudhisthira.wunder;

import android.os.Handler;
import android.os.Message;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yudhisthira on 14/05/17.
 */

public class CheckForInternetConnection {

    private Handler     mCallerThreadHandle;

    private Thread      mThread;

    public CheckForInternetConnection(Handler callerThreadHandle) {
        mCallerThreadHandle = callerThreadHandle;
    }

    public void start() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {

                boolean bAvailable = false;

                try {
                    String link = "http://www.google.co.in";
                    URL url = new URL(link);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.connect();

                    int statusCode = conn.getResponseCode();

                    conn.disconnect();

                    bAvailable = (statusCode == HttpURLConnection.HTTP_OK);
                }
                catch (Exception e) {

                }
                finally {

                }

                Message msg = Message.obtain();
                msg.what = 100;
                msg.obj = bAvailable;
                mCallerThreadHandle.sendMessage(msg);
            }
        });

        mThread.setDaemon(true);
        mThread.start();
    }

}

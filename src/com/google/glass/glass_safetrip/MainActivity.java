package com.google.glass.glass_safetrip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.glass.glass_safetrip.service.AccelerometerService;
import com.google.glass.glass_safetrip.service.AlljoynRemoteService;
import com.google.glass.glass_safetrip.service.SpeedCardService;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();

    private static final String API_URL = Constants.API_URL;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        // live card service
        startService(new Intent(this, SpeedCardService.class));

        // accelerometer service
        startService(new Intent(this, AccelerometerService.class));

        // alljoyn service
        startService(new Intent(this, AlljoynRemoteService.class));

        Log.d(TAG, "start service intent sent");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, AccelerometerService.class));
    }

}

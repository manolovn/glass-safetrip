package com.google.glass.glass_safetrip.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * glass
 * com.google.glass.glass_safetrip.service
 *
 * @autor manolo
 */
public class AccelerometerService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MyTimerTask timerTask;
    private Timer timer;

    /* Parameters to detect sleep gesture */
    private boolean gestureDetected = false;

    // Timelimit in milliseconds
    private int timeLimit = 3000;

    // Limits
    private float yMaxLimit = 8;
    private float zMinLimit = 2;

    @Override
    public void onCreate() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        Log.v("AccelerometerService", "x = "+x+" y = "+y+" z = "+ z);

        if (y < yMaxLimit || z > zMinLimit) {
            gestureDetected = true;
            // Call the delay method here
            timer = new Timer();
            timerTask = new MyTimerTask();
            timer.schedule(timerTask, timeLimit);
        } else {
            gestureDetected = false;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            if (gestureDetected)
                Log.v("AccelerometerService", "YOU ARE SLEEPING !!!");
        }
    }

}

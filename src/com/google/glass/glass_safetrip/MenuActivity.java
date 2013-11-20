package com.google.glass.glass_safetrip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.glass.glass_safetrip.api.ApiManager;
import com.google.glass.glass_safetrip.api.SafeTripApi;
import com.google.glass.glass_safetrip.entity.Accident;
import com.google.glass.glass_safetrip.entity.Emergency;
import com.google.glass.glass_safetrip.service.AccelerometerService;
import com.google.glass.glass_safetrip.service.SpeedCardService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * glass
 * com.google.glass.glass_safetrip
 *
 * @autor manolo
 */
public class MenuActivity extends Activity {

    private static final String TAG = MenuActivity.class.getName();

    private ApiManager apiManager;
    private SafeTripApi api;

    private LocationManager locationManager;

    private double latitude;
    private double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.apiManager = new ApiManager();
        this.api = apiManager.getApi();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 1000, 10, new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_a:
                // Report emergency
                this.api.addEmergency(longitude, latitude, "emergency", new Callback<Emergency>() {

                    @Override
                    public void success(Emergency emergency, Response response) {
                        Log.d(TAG, "addEmergency SUCCESSFULLY");
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.e(TAG, "addEmergency FAILED: " + retrofitError.getMessage());
                    }

                });
                return true;
            case R.id.option_b:
                // Report hazard
                this.api.addAccident(longitude, latitude, "accident", new Callback<Accident>() {
                    @Override
                    public void success(Accident accident, Response response) {
                        Log.d(TAG, "addAccident SUCCESSFULLY");
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.e(TAG, "addAccident FAILED: " + retrofitError.getMessage());
                    }

                });
                return true;
            case R.id.option_stop:
                stopService(new Intent(this, SpeedCardService.class));
                Log.d(TAG, "closing...");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.d(TAG, "menu closed");
        finish();
        Log.d(TAG, "finishing menu activity..");
    }

}

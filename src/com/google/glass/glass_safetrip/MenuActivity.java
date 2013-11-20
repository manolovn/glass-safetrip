package com.google.glass.glass_safetrip;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.glass.glass_safetrip.service.SpeedCardService;

/**
 * glass
 * com.google.glass.glass_safetrip
 *
 * @autor manolo
 */
public class MenuActivity extends Activity {

    private static final String TAG = MenuActivity.class.getName();

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
        finish();
    }

}

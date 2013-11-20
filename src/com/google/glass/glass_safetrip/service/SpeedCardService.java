package com.google.glass.glass_safetrip.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.RemoteViews;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.google.glass.glass_safetrip.MenuActivity;
import com.google.glass.glass_safetrip.R;
import com.google.glass.glass_safetrip.renderer.LiveCardRenderer;

/**
 * glass
 * com.google.glass.glass_safetrip.service
 *
 * @autor manolo
 */
public class SpeedCardService extends Service {

    private static final String TAG = SpeedCardService.class.getName();

    private LiveCardRenderer renderer;
    private LiveCard liveCard;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "on start command");

            if ( liveCard == null ) {

            TimelineManager tm = TimelineManager.from(getBaseContext());
            liveCard = tm.getLiveCard("1");

            liveCard.setNonSilent(true);
//            liveCard.enableDirectRendering(true);

            // create live card renderer for live card callbacks
//            if ( renderer == null ) {
//                renderer = new LiveCardRenderer();
//            }
//            liveCard.getSurfaceHolder().addCallback(renderer);


            RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.main_card);

            liveCard.setViews(views);

            Intent activityIntent = new Intent(getApplicationContext(), MenuActivity.class);
            liveCard.setAction(PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, 0));

            // finaly publish live card
            liveCard.publish();

            Log.d(TAG, "live card published");

        } else {
            Log.d(TAG, "live card already published");
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {


    }

}

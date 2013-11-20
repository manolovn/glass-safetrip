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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "on start command");

        LiveCard liveCard;

        TimelineManager tm = TimelineManager.from(getBaseContext());
        liveCard = tm.getLiveCard("1");

        liveCard.setNonSilent(true);
        liveCard.enableDirectRendering(true);

        // create live card renderer for live card callbacks
        if ( renderer == null ) {
            renderer = new LiveCardRenderer();
        }
        liveCard.getSurfaceHolder().addCallback(renderer);


        RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.main_card);

        liveCard.setViews(views);

        liveCard.setAction(PendingIntent.getActivity(getApplicationContext(), 0, intent, 0));

        // finaly publish live card
        liveCard.publish();

        Log.d(TAG, "live card published");

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {


    }

}

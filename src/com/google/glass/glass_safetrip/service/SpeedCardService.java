package com.google.glass.glass_safetrip.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.google.glass.glass_safetrip.Constants;
import com.google.glass.glass_safetrip.MenuActivity;
import com.google.glass.glass_safetrip.R;

/**
 * glass
 * com.google.glass.glass_safetrip.service
 *
 * @autor manolo
 */
public class SpeedCardService extends Service {

    private static final String TAG = SpeedCardService.class.getName();

    // private LiveCardRenderer renderer;
    private TimelineManager tm;
    private LiveCard liveCard;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = TimelineManager.from(this);
    }

    // @TODO: move to onStart method
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "on start command");

        if (liveCard == null) {

            // instantiate live card
            liveCard = tm.getLiveCard(Constants.LIVE_CARD_ID);

            // set non silent mode
            liveCard.setNonSilent(true);
//            liveCard.enableDirectRendering(true);

            // create live card renderer for live card callbacks
//            if ( renderer == null ) {
//                renderer = new LiveCardRenderer();
//            }
//            liveCard.getSurfaceHolder().addCallback(renderer);


            // create views to draw on live card
            RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.main_card);
            views.setTextViewText(R.id.textView2, "0.01 mph");
            liveCard.setViews(views);

            // prepare menu
            Intent activityIntent = new Intent(getApplicationContext(), MenuActivity.class);
            liveCard.setAction(PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, 0));

            // finally publish live card
            liveCard.publish();

            Log.d(TAG, "live card published");

        } else {

            Log.d(TAG, "live card already published");

        }

        return Service.START_STICKY;
    }

}

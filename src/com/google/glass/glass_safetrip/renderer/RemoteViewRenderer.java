package com.google.glass.glass_safetrip.renderer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.google.glass.glass_safetrip.Constants;
import com.google.glass.glass_safetrip.MenuActivity;
import com.google.glass.glass_safetrip.R;

import static com.google.glass.glass_safetrip.Constants.MAX_SPEED;

/**
 * glass-safetrip
 * com.google.glass.glass_safetrip.renderer
 *
 * @autor manolo
 */
public class RemoteViewRenderer {

    private static final String TAG = RemoteViewRenderer.class.getName();

    private LiveCard liveCard;
    private TimelineManager tm;
    private RemoteViews views;

    private Context mContext;

    public RemoteViewRenderer(Context context) {
        tm = TimelineManager.from(context);
        views = new RemoteViews(context.getPackageName(), R.layout.main_card);

        this.mContext = context;
    }

    public void publishSpeed(String speed) {

        if (liveCard == null) {

            // instantiate live card
            liveCard = tm.getLiveCard(Constants.LIVE_CARD_ID);

            // set non silent mode
            liveCard.setNonSilent(true);
//            liveCard.enableDirectRendering(true);

            updateMph(speed);

            // prepare menu
            Intent activityIntent = new Intent(mContext, MenuActivity.class);
            liveCard.setAction(PendingIntent.getActivity(mContext, 0, activityIntent, 0));

            // finally publish live card
            liveCard.publish();

            Log.d(TAG, "live card published");

        } else {

            Log.d(TAG, "live card already published");

            updateMph(speed);

        }
    }


    private void updateMph(String speed) {
        views.setTextViewText(R.id.textView2, speed + Constants.SPEED_UNIT);

        double speedNumber = Double.parseDouble(speed);
        if (speedNumber > MAX_SPEED) {
            views.setTextColor(R.id.textView2, Color.RED);
        } else {
            views.setTextColor(R.id.textView2, Color.GREEN);
        }

        liveCard.setViews(views);
    }


}

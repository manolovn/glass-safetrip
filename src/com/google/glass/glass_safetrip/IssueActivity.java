package com.google.glass.glass_safetrip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.glass.app.Card;
import com.google.android.glass.media.Camera;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.glass.glass_safetrip.util.StringUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * glass
 * com.google.glass.glass_safetrip
 *
 * @autor manolo
 */
public class IssueActivity extends Activity {

    private static final String TAG = IssueActivity.class.getName();

    private static final int CAMERA_ACTION_CODE = 11;

    private Bitmap m_bmpImage = null;
    private File output = null;

    private View cardView;
    private Card card;
    private ArrayList<String> voiceResults;

    // Gesture detector
    private GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        card = new Card(getBaseContext());

        voiceResults = getIntent().getExtras().getStringArrayList(RecognizerIntent.EXTRA_RESULTS);

        String text = StringUtil.ArrayStringToString(voiceResults);
        card.setText(text);

        card.setInfo(getString(R.string.app_name));
        cardView = card.toView();

        setContentView(cardView);
        Log.d(TAG, "issue activity created...");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Gesture detector for issue wizard
     *
     * @param context
     * @return
     */
    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    Log.d(TAG, "sending info...");
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    return true;
                }
                return false;
            }
        });
        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                return false;
            }
        });
        return gestureDetector;
    }

    /*
     * Send generic motion events to the gesture detector
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_CAMERA) {
            // Stop the preview and release the camera.
            // Execute your logic as quickly as possible
            // so the capture happens quickly.

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            output = new File(dir, Constants.TMP_FILE);
            Log.d(TAG, "image uri suggested: " + Uri.fromFile(output).toString());

//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
            startActivityForResult(takePictureIntent, CAMERA_ACTION_CODE);

            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bitmap tmpImage = null;
        Uri imageUri = null;
        Uri uriImage = null;

        String uriString = null;

        switch (requestCode) {

            case CAMERA_ACTION_CODE:
//                tmpImage = MediaHelper.extractBitmapFromIntent(this, intent);
//                imageUri = Uri.fromFile(output);
                FileObserver fileObserver = new FileObserver(uriString) {
                    @Override
                    public void onEvent(int event, final String path) {

                        Log.d(TAG, "file observer event: " + event);
                        Log.d(TAG, "file observer path: " + path);

                        switch (event) {
                            case FileObserver.MOVED_TO:
                            case FileObserver.MODIFY:
                            case FileObserver.CREATE:
                                card.addImage(Uri.fromFile(new File(path)));
                                //card.setFullScreenImages(true);
                                cardView = card.toView();
                                setContentView(cardView);
                                break;
                        }
                    }
                };
                uriString = intent.getStringExtra(Camera.EXTRA_PICTURE_FILE_PATH);
                fileObserver.startWatching();
                Log.d(TAG, "image uri recovered: " + uriString);
                break;

            default:
                return;

        }

        if (uriString != null) {

//        cardView.setBackgroundDrawable(new BitmapDrawable(getResources(), m_bmpImage));
//            card.addImage(imageUri);



        }

    }

}

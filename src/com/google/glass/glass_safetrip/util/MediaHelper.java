package com.google.glass.glass_safetrip.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.google.glass.glass_safetrip.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * glass-safetrip
 * com.google.glass.glass_safetrip.util
 *
 * @autor manolo
 */
public class MediaHelper {

    private static final String TAG = MediaHelper.class.getName();

    /**
     * @param context
     * @param intent
     * @return
     */
    public static Bitmap extractBitmapFromIntent(Context context, Intent intent) {

//		if ( intent == null ) {
//			Log.d("MEDIA HELPER", "extract bitmap from intent null");
//			return null;
//		}

        InputStream in = null;
        try {

            if (intent != null && intent.getData() != null) {

                Log.d(TAG, "intent " + intent.getData().toString());
                in = context.getContentResolver().openInputStream(intent.getData());

            } else {

                File file = new File(Environment.getExternalStorageDirectory(), Constants.TMP_FILE);

                Log.d(TAG, "camera tmp file " + file.toString());
                return BitmapFactory.decodeFile(file.getAbsolutePath());
//				return (Bitmap)intent.getExtras().get("data");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(in);
    }


    public static Bitmap resize(Bitmap tmpImage) {

        float scaleFactor;
        int w = tmpImage.getWidth();
        int h = tmpImage.getHeight();
        if (w > h)
            scaleFactor = ((float) Constants.MAX_WIDTH) / w;
        else
            scaleFactor = ((float) Constants.MAX_HEIGHT) / h;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor);
        return Bitmap.createBitmap(tmpImage, 0, 0, w, h, matrix, true);

    }


    public static Uri extractUriFromIntent(Context context, Intent intent) {
        Uri uri = intent.getData();
        return uri;
    }


    public static Uri extractFileUriFromIntent(Context context, Intent intent) {
        Uri uri = intent.getData();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return Uri.fromFile(new File(cursor.getString(column_index)));
    }


    public static Uri getPictureUriFromCamera() {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(), Constants.TMP_FILE));
    }

}

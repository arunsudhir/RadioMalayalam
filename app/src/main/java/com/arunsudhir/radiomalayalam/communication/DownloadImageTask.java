package com.arunsudhir.radiomalayalam.communication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.common.base.Function;

import java.io.InputStream;

/**
 * Created by Arun on 10/30/2015.
 */
public class DownloadImageTask<T> extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Function<Bitmap, T> function;
    Bitmap fallbackImage;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    public DownloadImageTask(Function<Bitmap, T> func, Bitmap fallBackImage) {
        this.function = func;
        this.fallbackImage = fallBackImage;
    }
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            return fallbackImage;
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if(function == null)
            bmImage.setImageBitmap(result);
        else
        {
            function.apply(result);
        }
    }
}

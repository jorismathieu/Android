package fr.zait.utils;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.io.InputStream;

public class ImageUtils {
    public static void loadImageByUrl(String imageUrl, ImageView imageView) {
        DownloadImageTask downloadImageTask = new DownloadImageTask(imageView, null, 0, 0, null);
        downloadImageTask.execute(imageUrl);
    }

    public static void loadImageInRemoteViewByUrl(String imageUrl, RemoteViews rmView, int imageViewId, int appWidgetId, Context context) {
        DownloadImageTask downloadImageTask = new DownloadImageTask(null, rmView, imageViewId, appWidgetId, context);
        downloadImageTask.execute(imageUrl);
    }


    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView bmImage;
        private RemoteViews rmViews;
        private int imageViewId;
        private int appWidgetId;
        private Context context;

        public DownloadImageTask(ImageView bmImage, RemoteViews rvImage, int imageViewId, int appWidgetId, Context context) {
            this.bmImage = bmImage;
            this.rmViews = rvImage;
            this.imageViewId = imageViewId;
            this.appWidgetId = appWidgetId;
            this.context = context;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (bmImage != null) {
                bmImage.setImageBitmap(result);
            } else if (rmViews != null) {
                ;
            }
            {
                rmViews.setImageViewBitmap(imageViewId, result);
                AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(appWidgetId, rmViews);
            }
        }
    }

}

package club.tushar.hdwallpaper.services;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import club.tushar.hdwallpaper.R;
import club.tushar.hdwallpaper.db.AppDatabase;
import club.tushar.hdwallpaper.db.Wallpapers;
import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;
import club.tushar.hdwallpaper.utils.Constants;

public class ChangeWallPaperAlarmReceiver extends BroadcastReceiver {

    private Context context;
    private WallpaperManager myWallpaperManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        new ChangeWallpaper(context).execute();
    }

    private class ChangeWallpaper extends AsyncTask<URL, Integer, Bitmap> {

        private Context context;

        public ChangeWallpaper(Context context) {
            this.context = context;
            myWallpaperManager = WallpaperManager.getInstance(context);
        }

        @Override
        protected Bitmap doInBackground(URL... urls){
            Bitmap myBitmap = null;
            try{

                Wallpapers wallpapers = AppDatabase.getInstance(context).daoWallpapers().getRandomWallpapers();
                myBitmap = BitmapFactory.decodeFile(wallpapers.getPath());
                myWallpaperManager.setBitmap(myBitmap);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    myWallpaperManager.setBitmap(myBitmap, null, false, WallpaperManager.FLAG_LOCK);
                }
                Log.e("changed", "changed wall paper");
            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }


            return myBitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap){
            super.onPostExecute(bitmap);
        }
    }
}
package club.tushar.hdwallpaper.services;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Executors;

import club.tushar.hdwallpaper.db.AppDatabase;
import club.tushar.hdwallpaper.db.Photo;
import club.tushar.hdwallpaper.db.Wallpapers;
import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;
import club.tushar.hdwallpaper.services.jobs.DownloadPictureJobService;
import club.tushar.hdwallpaper.utils.Constants;

public class ImageDownloadAlarmReceiver extends BroadcastReceiver {

    private Context context;
    private WallpaperManager myWallpaperManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("done", System.currentTimeMillis() + "");
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Photo photo = AppDatabase.getInstance(context).daoWallpapers().getRandomPhoto();
                DownloadPictureJobService.startActionDownloadImage(context, photo.getLink(), photo.getId() + "");
            }
        });

    }

}
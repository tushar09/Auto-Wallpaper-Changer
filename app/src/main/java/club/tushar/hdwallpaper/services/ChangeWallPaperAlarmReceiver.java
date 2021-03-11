package club.tushar.hdwallpaper.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import club.tushar.hdwallpaper.services.jobs.ChangeWallpaperJobService;
import club.tushar.hdwallpaper.utils.Constants;

public class ChangeWallPaperAlarmReceiver extends BroadcastReceiver {

    private Context context;
    private WallpaperManager myWallpaperManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent changeWallpaperAlarmIntent = new Intent(context, ChangeWallPaperAlarmReceiver.class);
            PendingIntent changeWallpaperPendingIntent = PendingIntent.getBroadcast(context, 1, changeWallpaperAlarmIntent, 0);
            AlarmManager changeManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //int changeInterval = Constants.getSharedPreferences(context).getTimerAutoChange() * 3600 * 1000;
            int changeInterval = 1800 * 1000;
            changeManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + changeInterval, changeWallpaperPendingIntent);
            Log.e("execute", "exe");
        }

        ChangeWallpaperJobService.changeWallpaper(context);
    }
}
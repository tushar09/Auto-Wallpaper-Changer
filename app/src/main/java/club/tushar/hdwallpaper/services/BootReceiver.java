package club.tushar.hdwallpaper.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import club.tushar.hdwallpaper.utils.Constants;

public class BootReceiver extends BroadcastReceiver {

    private PendingIntent pendingIntent;
    private PendingIntent changeWallpaperPendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e("boot", "received");
            /* Retrieve a PendingIntent that will perform a broadcast */
            Intent alarmIntent = new Intent(context, ImageDownloadAlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int interval = 3600 * 1000;
            //int interval = 8000;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent);
            }else {
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            }
            //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

            /* Retrieve a PendingIntent that will perform a broadcast */
            Intent changeWallpaperAlarmIntent = new Intent(context, ChangeWallPaperAlarmReceiver.class);
            changeWallpaperPendingIntent = PendingIntent.getBroadcast(context, 1, changeWallpaperAlarmIntent, 0);
            AlarmManager changeManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int changeInterval = Constants.getSharedPreferences(context).getTimerAutoChange() * 3600 * 1000;
            //int changeInterval = 8000;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                changeManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, changeWallpaperPendingIntent);
            }else {
                changeManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), changeInterval, changeWallpaperPendingIntent);
            }
            //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        }

    }
}
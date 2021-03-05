package club.tushar.hdwallpaper.services.jobs;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.File;

import club.tushar.hdwallpaper.db.AppDatabase;
import club.tushar.hdwallpaper.db.Wallpapers;
import club.tushar.hdwallpaper.utils.Constants;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ChangeWallpaperJobService extends JobIntentService {

    private WallpaperManager myWallpaperManager;

    public static void changeWallpaper(Context context){
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
        enqueueWork(context, ChangeWallpaperJobService.class, 2, new Intent());
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if(Constants.getSharedPreferences(this).isEnabledAutoChange()){
            Bitmap myBitmap = null;
            try{
                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(this);
                Wallpapers wallpapers = AppDatabase.getInstance(this).daoWallpapers().getRandomWallpapers();
                myBitmap = BitmapFactory.decodeFile(wallpapers.getPath());
                myWallpaperManager.setBitmap(myBitmap);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Constants.getSharedPreferences(this).isEnabledScreenLockAutoChange()){
                    myWallpaperManager.setBitmap(myBitmap, null, false, WallpaperManager.FLAG_LOCK);
                }
                File f = new File(wallpapers.getPath());
                if(f.delete()){
                    AppDatabase.getInstance(this).daoWallpapers().delete(wallpapers);
                }
                Log.e("wallpaper", "Changed");
            }catch(Exception e){
                Log.e("wallpaper err", e.toString());
                e.printStackTrace();
            }
        }
    }
}
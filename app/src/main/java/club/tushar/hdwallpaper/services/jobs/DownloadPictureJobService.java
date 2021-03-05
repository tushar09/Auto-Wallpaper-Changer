package club.tushar.hdwallpaper.services.jobs;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import club.tushar.hdwallpaper.db.AppDatabase;
import club.tushar.hdwallpaper.db.Wallpapers;
import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;
import club.tushar.hdwallpaper.utils.Constants;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownloadPictureJobService extends JobIntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DOWNLOAD_IMAGE = "club.tushar.hdwallpaper.services.action.ACTION_DOWNLOAD_IMAGE";
    private static final String ACTION_BAZ = "club.tushar.hdwallpaper.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM_IMAGE_URL = "club.tushar.hdwallpaper.services.extra.LINK";
    private static final String EXTRA_PARAM_IMAGE_ID = "club.tushar.hdwallpaper.services.extra.IMAGE_ID";

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        final String imageUrl = intent.getStringExtra(EXTRA_PARAM_IMAGE_URL);
        final String imageId = intent.getStringExtra(EXTRA_PARAM_IMAGE_ID);
        handleActionFoo(imageUrl, imageId);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionDownloadImage(Context context, String imageUrl, String id) {
        Intent intent = new Intent(context, DownloadPictureJobService.class);
        intent.setAction(ACTION_DOWNLOAD_IMAGE);
        intent.putExtra(EXTRA_PARAM_IMAGE_URL, imageUrl);
        intent.putExtra(EXTRA_PARAM_IMAGE_ID, id);
        enqueueWork(context, DownloadPictureJobService.class, 1, intent);
        Log.e("service", "started");
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String imageUrl, String imageId) {

        Bitmap myBitmap = null;
        try{
            PixelsResponse pixelsResponse = Constants.getSharedPreferences(this).getResponse();
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setDoInput(true);
            connection.connect();
            //InputStream input = new BufferedInputStream(url.openStream(), 8192);
            InputStream input = connection.getInputStream();

            byte data[] = new byte[1024];
            int count;

            ByteArrayOutputStream imageBaos = new ByteArrayOutputStream();

            while((count = input.read(data)) != -1){
                imageBaos.write(data, 0, count);
            }
            myBitmap = BitmapFactory.decodeByteArray(imageBaos.toByteArray(), 0, imageBaos.size());

            input.close();
            imageBaos.flush();
            imageBaos.close();
            connection.disconnect();

            File f = new File(getDir("myFiles", Context.MODE_PRIVATE), imageId + "");
            f.createNewFile();

            //Convert bitmap to byte array
            Bitmap bitmap = myBitmap;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();

            Wallpapers wallpapers = new Wallpapers(f.getPath());
            AppDatabase.getInstance(this).daoWallpapers().insert(wallpapers);
            Log.e("service path", wallpapers.getPath());
            Log.e("download", "done");
        }catch(MalformedURLException e){
            e.printStackTrace();
            Log.e("MalformedURLException", e.toString());
        }catch(IOException e){
            e.printStackTrace();
            Log.e("IOException", e.toString());
        }catch(RuntimeException e){
            e.printStackTrace();
            Log.e("IOException", e.toString());
        }
    }
}
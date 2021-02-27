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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import club.tushar.hdwallpaper.R;
import club.tushar.hdwallpaper.dto.downImage.DownloadImage;
import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;
import club.tushar.hdwallpaper.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmReceiver extends BroadcastReceiver {

    private Context context;
    private WallpaperManager myWallpaperManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("done", System.currentTimeMillis() + "");
        Random r = new Random();
        int low = 0;
        int high = 79;
        int result = r.nextInt(high-low) + low;
        loadNextWallpaper(context, result);
    }

    private class DownloadBitMap extends AsyncTask<URL, Integer, Bitmap> {

        private int result;
        private Context context;

        public DownloadBitMap(int result, Context context) {
            this.result = result;
            this.context = context;
            myWallpaperManager = WallpaperManager.getInstance(context);
        }

        @Override
        protected Bitmap doInBackground(URL... urls){
            Bitmap myBitmap = null;
            try{
                PixelsResponse pixelsResponse = Constants.getSharedPreferences(context).getResponse();
                URL url = new URL(pixelsResponse.getPhotos().get(result).getSrc().getOriginal());
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
            try {
                myWallpaperManager.setBitmap(bitmap);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    myWallpaperManager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK);
                }
                Log.e("err", "Finished");
            } catch (IOException e) {
                Log.e("err", e.toString());
                e.printStackTrace();
            }

        }
    }

    private void loadNextWallpaper(Context context, int result) {
        new DownloadBitMap(result, context).execute();
    }

}
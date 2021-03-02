package club.tushar.hdwallpaper.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import club.tushar.hdwallpaper.adapter.HomeAdapterNew;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import club.tushar.hdwallpaper.R;
import club.tushar.hdwallpaper.databinding.ActivityHomeBinding;
import club.tushar.hdwallpaper.databinding.DialogDetailsBelow24Binding;
import club.tushar.hdwallpaper.db.AppDatabase;
import club.tushar.hdwallpaper.db.Wallpapers;
import club.tushar.hdwallpaper.dto.mainHomeModel.MainModelResponseDto;
import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;
import club.tushar.hdwallpaper.services.ChangeWallPaperAlarmReceiver;
import club.tushar.hdwallpaper.services.ImageDownloadAlarmReceiver;
import club.tushar.hdwallpaper.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity{

    private ActivityHomeBinding binding;

    public static HomeActivity ha;

    private MaterialDialog dialog;

    private WallpaperManager myWallpaperManager;

    private ProgressDialog pd;

    private List<MainModelResponseDto> mainModelResponseDtos;
    private List<String> url;

    private HomeAdapterNew adapterNew;

    private String id;
    private RecyclerView.LayoutManager gridLayoutManager;

    private PendingIntent pendingIntent;
    private PendingIntent changeWallpaperPendingIntent;

    private File directory;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        directory = getDir("myFiles", Context.MODE_PRIVATE);
        directory.mkdir();


        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(this, ImageDownloadAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 8000;
        //int interval = 43200000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent changeWallpaperAlarmIntent = new Intent(this, ChangeWallPaperAlarmReceiver.class);
        changeWallpaperPendingIntent = PendingIntent.getBroadcast(this, 0, changeWallpaperAlarmIntent, 0);
        AlarmManager changeManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int changeInterval = 8000;
        //int interval = 43200000;
        changeManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), changeInterval, changeWallpaperPendingIntent);
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

        binding.tvTitile.setTextColor(Color.parseColor("#800CDD"));
        Shader textShader=new LinearGradient(0, 0, binding.tvTitile.getPaint().measureText(getString(R.string.app_name)), binding.tvTitile.getTextSize(),
                new int[]{Color.parseColor("#800CDD"), Color.parseColor("#3BA3F2")},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        binding.tvTitile.getPaint().setShader(textShader);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.container.rvList.setLayoutManager(gridLayoutManager);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage(getResources().getString(R.string.appling_picture));

        ha = this;

        myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        loadMoreByPage(1);


        binding.fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showEndView(binding.fab, binding.llSetting);
            }
        });
    }

    private void showEndView(View startView, View endView) {
        // Construct a container transform transition between two views.
        MaterialContainerTransform transition = new MaterialContainerTransform();
        transition.setScrimColor(Color.TRANSPARENT);
        transition.setPathMotion(new MaterialArcMotion());
        transition.setInterpolator(new FastOutSlowInInterpolator());
        transition.setDuration(500);
        transition.addListener(new TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
                //transition.
            }
        });
        //set the duration....

        //Define the start and the end view
        transition.setStartView(startView);
        transition.setEndView(endView);
        transition.addTarget(endView);

        // Trigger the container transform transition.

        TransitionManager.beginDelayedTransition(binding.root, transition);
        startView.setVisibility(View.GONE);
        endView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        if(binding.llSetting.getVisibility() == View.VISIBLE){
            showEndView(binding.llSetting, binding.fab);
        }else {
            super.onBackPressed();
        }
    }

    public void downloadPicture(final String regularUrl, String large2x, int id){

        this.id = id + "";

        DialogDetailsBelow24Binding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_details_below_24, null, true);

        final Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(binding.getRoot());
        Glide.with(this).load(large2x).into(binding.ivImage);
        binding.btNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                customDialog.dismiss();
            }
        });
        binding.btYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                try{
                    customDialog.dismiss();
                    dialog = new MaterialDialog.Builder(HomeActivity.this)
                            .title(R.string.downloading)
                            .content(R.string.please_wait)
                            .progress(false, 100, true)
                            .icon(ContextCompat.getDrawable(HomeActivity.this, R.mipmap.ic_launcher))
                            .show();
                    new DownloadBitMap().execute(new URL(regularUrl), null, null);
                    //Log.e("regularUrl", fullUrl);
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }

            }
        });

        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.show();

        Window window = customDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private class DownloadBitMap extends AsyncTask<URL, Integer, Bitmap>{

        @Override
        protected Bitmap doInBackground(URL... urls){

            URL url = null;
            int fileLength = 0;
            Bitmap myBitmap = null;
            try{
                url = urls[0];

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.setDoInput(true);
                connection.connect();
                //InputStream input = new BufferedInputStream(url.openStream(), 8192);
                InputStream input = connection.getInputStream();

                fileLength = connection.getContentLength();
                byte data[] = new byte[1024];
                long total = 0;
                int count;

                ByteArrayOutputStream imageBaos = new ByteArrayOutputStream();

                while((count = input.read(data)) != -1){
                    total += count;
                    // publishing the progress....
                    imageBaos.write(data, 0, count);
                    publishProgress((int) (total * 100 / fileLength));
                }
                myBitmap = BitmapFactory.decodeByteArray(imageBaos.toByteArray(), 0, imageBaos.size());

                input.close();
                imageBaos.flush();
                imageBaos.close();
                connection.disconnect();

                File f = new File(directory, id);
                f.createNewFile();

                //Convert bitmap to byte array
                Bitmap bitmap = myBitmap;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();

                Wallpapers wallpapers = new Wallpapers(f.getPath());
                AppDatabase.getInstance(HomeActivity.this).daoWallpapers().insert(wallpapers);
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
            dialog.setProgress(values[0]);
            //Log.e("progre+ss", values[0] + " " + dialog.getMaxProgress() + " " + dialog.getCurrentProgress());
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap){
            super.onPostExecute(bitmap);

            dialog.dismiss();
            pd.show();
            new Thread(new Runnable(){
                @Override
                public void run(){
                    try{
                        pd.setMessage(getResources().getString(R.string.appling_picture_home));
                        myWallpaperManager.setBitmap(bitmap);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run(){
                                    pd.setMessage(getResources().getString(R.string.appling_picture_lock));
                                }
                            });

                            myWallpaperManager.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK);
                        }
                        pd.dismiss();
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }


    public void loadMoreByPage(int page){

        Constants.getApiService().getHome2("563492ad6f917000010000010676d489db5b43738c2e002114eaa93f","mobile wallpaper", 1, 80).enqueue(new Callback<PixelsResponse>() {
            @Override
            public void onResponse(Call<PixelsResponse> call, Response<PixelsResponse> response) {
                adapterNew = new HomeAdapterNew(HomeActivity.this, response.body());
                binding.container.rvList.setAdapter(adapterNew);
                Constants.getSharedPreferences(HomeActivity.this).setResponse(response.body());
            }

            @Override
            public void onFailure(Call<PixelsResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

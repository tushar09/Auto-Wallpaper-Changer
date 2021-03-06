package club.tushar.hdwallpaper.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import club.tushar.hdwallpaper.R;
import club.tushar.hdwallpaper.adapter.HomeAdapterNew;
import club.tushar.hdwallpaper.databinding.ActivityHomeBinding;
import club.tushar.hdwallpaper.databinding.DialogDetailsBelow24Binding;
import club.tushar.hdwallpaper.databinding.DownloaderDialogBinding;
import club.tushar.hdwallpaper.databinding.TimerDialogBinding;
import club.tushar.hdwallpaper.db.AppDatabase;
import club.tushar.hdwallpaper.db.Photo;
import club.tushar.hdwallpaper.db.Wallpapers;
import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;
import club.tushar.hdwallpaper.services.ChangeWallPaperAlarmReceiver;
import club.tushar.hdwallpaper.services.ImageDownloadAlarmReceiver;
import club.tushar.hdwallpaper.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    public static HomeActivity ha;

    private AlertDialog dialog;

    private WallpaperManager myWallpaperManager;

    private ProgressDialog pd;

    private HomeAdapterNew adapterNew;

    private String id;
    private RecyclerView.LayoutManager gridLayoutManager;

    private PendingIntent pendingIntent;
    private PendingIntent changeWallpaperPendingIntent;

    private File directory;

    private DownloaderDialogBinding downloaderDialogBinding;

    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int pageCount = 0;
    private PixelsResponse pixelsResponse = new PixelsResponse();
    private List<PixelsResponse.Photo> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        int interval = 3600 * 1000;
        //int interval = 8000;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent changeWallpaperAlarmIntent = new Intent(this, ChangeWallPaperAlarmReceiver.class);
        changeWallpaperPendingIntent = PendingIntent.getBroadcast(this, 1, changeWallpaperAlarmIntent, 0);
        AlarmManager changeManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int changeInterval = Constants.getSharedPreferences(this).getTimerAutoChange() * 3600 * 1000;
        //int changeInterval = 8000;

        changeManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), changeInterval, changeWallpaperPendingIntent);
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);


        binding.tvTitile.setTextColor(Color.parseColor("#800CDD"));
        Shader textShader = new LinearGradient(0, 0, binding.tvTitile.getPaint().measureText(getString(R.string.app_name)), binding.tvTitile.getTextSize(),
                new int[]{Color.parseColor("#800CDD"), Color.parseColor("#3BA3F2")},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        binding.tvTitile.getPaint().setShader(textShader);

        pixelsResponse.setPhotos(photos);
        adapterNew = new HomeAdapterNew(HomeActivity.this, pixelsResponse);
        binding.container.rvList.setAdapter(adapterNew);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.container.rvList.setLayoutManager(gridLayoutManager);


        binding.container.rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            pageCount++;
                            loadMoreByPage(pageCount);
                        }
                    }
                }
            }
        });

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage(getResources().getString(R.string.appling_picture));

        ha = this;

        myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        pageCount++;
        loadMoreByPage(pageCount);

        setSetting();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndView(binding.fab, binding.llSetting);
            }
        });

        binding.hEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constants.getSharedPreferences(HomeActivity.this).setEnableAutoChange(isChecked);
            }
        });

        binding.slEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constants.getSharedPreferences(HomeActivity.this).setEnableScreenLockAutoChange(isChecked);
            }
        });

        binding.llTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerDialogBinding timerDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(HomeActivity.this), R.layout.timer_dialog, null, false);
                timerDialogBinding.rgTimer.clearCheck();
                switch (Constants.getSharedPreferences(HomeActivity.this).getTimerAutoChange()) {
                    case 4:
                        timerDialogBinding.rb4Hrs.setChecked(true);
                        break;
                    case 6:
                        timerDialogBinding.rb6Hrs.setChecked(true);
                        break;
                    case 12:
                        timerDialogBinding.rb12Hrs.setChecked(true);
                        break;
                    case 24:
                        timerDialogBinding.rb24Hrs.setChecked(true);
                        break;
                }

                timerDialogBinding.rgTimer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb4Hrs:
                                Constants.getSharedPreferences(HomeActivity.this).setTimerAutoChange(4);
                                break;
                            case R.id.rb6Hrs:
                                Constants.getSharedPreferences(HomeActivity.this).setTimerAutoChange(6);
                                break;
                            case R.id.rb12Hrs:
                                Constants.getSharedPreferences(HomeActivity.this).setTimerAutoChange(12);
                                break;
                            case R.id.rb24Hrs:
                                Constants.getSharedPreferences(HomeActivity.this).setTimerAutoChange(24);
                                break;
                        }
                    }
                });

                new AlertDialog.Builder(HomeActivity.this)
                        .setView(timerDialogBinding.getRoot())
                        .setTitle("Set Timer")
                        .setMessage("Change Wallpaper in every: ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setSetting();
                            }
                        })
                        .setIcon(R.mipmap.ic_launcher)
                        .show()
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                setSetting();
                            }
                        });
            }
        });
    }

    private void setSetting() {
        binding.hEnable.setChecked(Constants.getSharedPreferences(this).isEnabledAutoChange());
        binding.slEnable.setChecked(Constants.getSharedPreferences(this).isEnabledScreenLockAutoChange());
        binding.tvTimer.setText("Change wallpaper in every " + Constants.getSharedPreferences(this).getTimerAutoChange() + " Hours");
    }

    private void showEndView(View startView, View endView) {
        // Construct a container transform transition between two views.
        MaterialContainerTransform transition = new MaterialContainerTransform();
        transition.setScrimColor(Color.TRANSPARENT);
        transition.setPathMotion(new MaterialArcMotion());
        transition.setInterpolator(new FastOutSlowInInterpolator());
        transition.setDuration(500);
        transition.addListener(new TransitionListenerAdapter() {
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
        if (binding.llSetting.getVisibility() == View.VISIBLE) {
            showEndView(binding.llSetting, binding.fab);
        } else {
            super.onBackPressed();
        }
    }

    public void downloadPicture(PixelsResponse.Photo photo) {

        this.id = photo.getId() + "";

        DialogDetailsBelow24Binding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_details_below_24, null, true);
        binding.tvCredit.setText("Photo by " + photo.getPhotographer() + " on Pixel");
        try {
            binding.cvCredit.setCardBackgroundColor(Color.parseColor("#66" + photo.getAvgColor().replaceAll("#", "")));
        } catch (Exception e) {
            binding.cvCredit.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        Glide.with(this).load(photo.getSrc().getLarge2x()).into(binding.ivImage);
        AlertDialog customDialog = new MaterialAlertDialogBuilder(HomeActivity.this)
                .setCancelable(false)
                .setView(binding.getRoot())
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        try {
                            downloaderDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(HomeActivity.this), R.layout.downloader_dialog, null, false);
                            dialog = new MaterialAlertDialogBuilder(HomeActivity.this)
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setTitle(R.string.downloading)
                                    .setMessage(R.string.please_wait)
                                    .setView(downloaderDialogBinding.getRoot())
                                    .show();
                            new DownloadBitMap().execute(new URL(photo.getSrc().getOriginal()), null, null);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .show();

        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.show();

        Window window = customDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private class DownloadBitMap extends AsyncTask<URL, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... urls) {

            URL url = null;
            int fileLength = 0;
            Bitmap myBitmap = null;
            try {
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

                while ((count = input.read(data)) != -1) {
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();

                Wallpapers wallpapers = new Wallpapers(f.getPath());
                AppDatabase.getInstance(HomeActivity.this).daoWallpapers().insert(wallpapers);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return myBitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //dialog.setProgress(values[0]);
            downloaderDialogBinding.progress.setProgressCompat(values[0], true);
            //Log.e("progre+ss", values[0] + " " + dialog.getMaxProgress() + " " + dialog.getCurrentProgress());
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            super.onPostExecute(bitmap);

            dialog.dismiss();
            pd.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        pd.setMessage(getResources().getString(R.string.appling_picture_home));
                        myWallpaperManager.setBitmap(bitmap);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.setMessage(getResources().getString(R.string.appling_picture_lock));
                                }
                            });
                            // 1. Get
                            // screen size.
                            DisplayMetrics metrics = new DisplayMetrics();
                            Display display = getWindowManager().getDefaultDisplay();
                            display.getMetrics(metrics);
                            final int screenWidth = metrics.widthPixels;
                            final int screenHeight = metrics.heightPixels;

                            float multipleFactor;
                            float h = (float) bitmap.getHeight() / screenHeight;
                            float w = (float) bitmap.getWidth() / screenWidth;

                            if (h < w) {
                                multipleFactor = h;
                            } else {
                                multipleFactor = w;
                            }
                            int shift = bitmap.getWidth() / 4;
                            Rect rect = new Rect();
                            rect.left = shift;
                            rect.top = 0;
                            rect.right = shift + Math.abs((int) (screenWidth * multipleFactor));
                            rect.bottom = (int) (screenHeight * multipleFactor);
                            myWallpaperManager.setBitmap(bitmap, rect, false, WallpaperManager.FLAG_LOCK);
                        }
                        pd.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("wall err", e.toString());
                    }

                }
            }).start();
        }
    }


    public void loadMoreByPage(int page) {
        loading = false;
        Constants.getApiService().getHome2("563492ad6f917000010000010676d489db5b43738c2e002114eaa93f", "mobile wallpaper", page, 80).enqueue(new Callback<PixelsResponse>() {
            @Override
            public void onResponse(Call<PixelsResponse> call, Response<PixelsResponse> response) {
                if (pixelsResponse.getPhotos() == null) {
                    pixelsResponse.setPhotos(response.body().getPhotos());
                } else {
                    pixelsResponse.getPhotos().addAll(response.body().getPhotos());
                }
                adapterNew.notifyDataSetChanged();
                List<Photo> photos = new ArrayList<>();
                for (int i = 0; i < response.body().getPhotos().size(); i++) {
                    Photo photo = new Photo();
                    photo.setLink(response.body().getPhotos().get(i).getSrc().getOriginal());
                    photos.add(photo);
                }
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(HomeActivity.this).daoWallpapers().insert(photos);
                        Log.e("size", AppDatabase.getInstance(HomeActivity.this).daoWallpapers().getPhotos().size() + "");
                    }
                });
                Constants.getSharedPreferences(HomeActivity.this).setResponse(response.body());
                loading = true;
            }

            @Override
            public void onFailure(Call<PixelsResponse> call, Throwable t) {
                loading = true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

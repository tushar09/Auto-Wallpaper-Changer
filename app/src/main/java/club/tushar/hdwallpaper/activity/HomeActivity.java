package club.tushar.hdwallpaper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.media.RemoteController;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import club.tushar.hdwallpaper.R;
import club.tushar.hdwallpaper.adapter.HomeAdapter;
import club.tushar.hdwallpaper.databinding.ActivityHomeBinding;
import club.tushar.hdwallpaper.databinding.DialogDetailsBelow24Binding;
import club.tushar.hdwallpaper.dto.downImage.DownloadImage;
import club.tushar.hdwallpaper.dto.mainHomeModel.MainModelResponseDto;
import club.tushar.hdwallpaper.dto.unPlash.HomeResponseDto;
import club.tushar.hdwallpaper.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

public class HomeActivity extends AppCompatActivity{

    private ActivityHomeBinding binding;

    public static HomeActivity ha;

    private MaterialDialog dialog;

    private WallpaperManager myWallpaperManager;

    private ProgressDialog pd;

    private List<MainModelResponseDto> mainModelResponseDtos;
    private List<String> url;

    private HomeAdapter adapter;

    List<HomeResponseDto.Hits> dtos;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binding.tvTitile.setTextColor(Color.parseColor("#800CDD"));
        Shader textShader=new LinearGradient(0, 0, binding.tvTitile.getPaint().measureText(getString(R.string.app_name)), binding.tvTitile.getTextSize(),
                new int[]{Color.parseColor("#800CDD"), Color.parseColor("#3BA3F2")},
                new float[]{0, 1}, Shader.TileMode.CLAMP);
        binding.tvTitile.getPaint().setShader(textShader);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage(getResources().getString(R.string.appling_picture));

        ha = this;

        dtos = new ArrayList<>();
        adapter = new HomeAdapter(HomeActivity.this, dtos);
        binding.container.gvList.setAdapter(adapter);

        myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        loadMoreByPage(1);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }
        });
    }

    public void downloadPicture(final String regularUrl, final String id, final String fullUrl){

        this.id = id;

        DialogDetailsBelow24Binding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_details_below_24, null, true);

        final Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(false);
        customDialog.setContentView(binding.getRoot());
        Glide.with(this).load(regularUrl).into(binding.ivImage);
        //Picasso.get().load(regularUrl).into(binding.ivImage);
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
                    new DownloadBitMap().execute(new URL(fullUrl), null, null);
                    Log.e("regularUrl", fullUrl);
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

                Log.e("file length", fileLength + "");

                ByteArrayOutputStream imageBaos = new ByteArrayOutputStream();

                while((count = input.read(data)) != -1){
                    total += count;
                    Log.e("length", total + " " + count + "");
                    // publishing the progress....
                    imageBaos.write(data, 0, count);
                    publishProgress((int) (total * 100 / fileLength));
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

            Constants.getApiService().getDownloadLocation(id).enqueue(new Callback<DownloadImage>(){
                @Override
                public void onResponse(Call<DownloadImage> call, Response<DownloadImage> response){
                    //Log.e("regularUrl", response.body());
                }

                @Override
                public void onFailure(Call<DownloadImage> call, Throwable t){
                    Log.e("regularUrl", t.toString());
                }
            });


        }
    }


    public void loadMoreByPage(int page){

        String url = Constants.BASE_URL + "&image_type=all&per_page=100&page=" + page;
        Constants.getApiService().getHome(url).enqueue(new Callback<HomeResponseDto>(){
            //Constants.getApiService().getrandomPhoto(30, Constants.orientation).enqueue(new Callback<List<HomeResponseDto>>(){
            //Constants.getApiService().searchPhoto("animal", 10).enqueue(new Callback<List<HomeResponseDto>>(){
            @Override
            public void onResponse(Call<HomeResponseDto> call, Response<HomeResponseDto> response){
                dtos.addAll(response.body().getHits());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<HomeResponseDto> call, Throwable t){

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

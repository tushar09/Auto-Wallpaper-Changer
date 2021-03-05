package club.tushar.hdwallpaper.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;


@Database(entities = {Wallpapers.class, Photo.class}, version = 2)
@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "wallpaper.db";

    private static volatile AppDatabase INSTANCE;

    public abstract DaoWallpapers daoWallpapers();

    public static AppDatabase getInstance(@NonNull Context appContext) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null)
                    INSTANCE = buildDatabase(appContext);
            }
        }

        return INSTANCE;
    }

    private static AppDatabase buildDatabase(Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                loadJSONFromAsset(db, appContext);
                            }
                        });

                    }
                })
                .build();
    }

    public static void loadJSONFromAsset(SupportSQLiteDatabase db, Context appContext) {
        String json = null;
        try {
            InputStream is = appContext.getAssets().open("dataLoad.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PixelsResponse pr = new Gson().fromJson(json, PixelsResponse.class);
        List<Photo> photos = new ArrayList<>();
        for (int i = 0; i < pr.getPhotos().size(); i++) {
            Photo photo = new Photo();
            photo.setLink(pr.getPhotos().get(i).getSrc().getOriginal());
            photos.add(photo);
        }

        AppDatabase.getInstance(appContext).daoWallpapers().insert(photos);
    }
}

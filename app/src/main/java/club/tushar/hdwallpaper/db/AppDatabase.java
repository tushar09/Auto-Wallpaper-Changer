package club.tushar.hdwallpaper.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Wallpapers.class}, version = 1)
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
                .build();
    }
}

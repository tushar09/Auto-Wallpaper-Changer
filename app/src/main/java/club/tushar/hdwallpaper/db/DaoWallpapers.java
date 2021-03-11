package club.tushar.hdwallpaper.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoWallpapers {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Wallpapers wallpapers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Photo> photos);

    @Query("SELECT * FROM wallpapers ORDER BY id ASC LIMIT 1")
    Wallpapers getNext();

    @Query("SELECT * FROM wallpapers")
    List<Wallpapers> getWallpapers();

    @Query("SELECT * FROM photo")
    List<Photo> getPhotos();


    @Query("SELECT * FROM wallpapers order by random() limit 1")
    Wallpapers getRandomWallpapers();

    @Query("SELECT * FROM photo order by random() limit 1")
    Photo getRandomPhoto();

    @Query("DELETE FROM wallpapers WHERE id = :id")
    void delete(long id);


}

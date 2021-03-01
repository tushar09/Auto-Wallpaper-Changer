package club.tushar.hdwallpaper.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "wallpapers")
public class Wallpapers {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String path;

    public Wallpapers(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

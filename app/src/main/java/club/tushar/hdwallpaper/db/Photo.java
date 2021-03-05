package club.tushar.hdwallpaper.db;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo", indices = {@Index(value = "link", unique = true)})
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

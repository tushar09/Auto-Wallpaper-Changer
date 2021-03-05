package club.tushar.hdwallpaper.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converter {
    @TypeConverter
    public static List<Photo> stringToPhotos(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Photo>>() {}.getType();
        List<Photo> measurements = gson.fromJson(json, type);
        return measurements;
    }

    @TypeConverter
    public static String measurementsToString(List<Photo> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Photo>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}

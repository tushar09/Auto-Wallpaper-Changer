package club.tushar.hdwallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;

public class SPreferences {

    private String RESPONSE = "response";

    private SharedPreferences sp;

    public SPreferences(Context c) {
        this.sp = c.getSharedPreferences(c.getPackageName(), Context.MODE_PRIVATE);
    }

    public void setResponse(PixelsResponse response){
        sp.edit().putString(RESPONSE, new Gson().toJson(response)).commit();
    }

    public PixelsResponse getResponse(){
        return new Gson().fromJson(sp.getString(RESPONSE, "na"), PixelsResponse.class);
    }
}

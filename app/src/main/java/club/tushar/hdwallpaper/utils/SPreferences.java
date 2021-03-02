package club.tushar.hdwallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;

public class SPreferences {

    private String RESPONSE = "response";

    private String ENABLE_SCREENLOCK_AUTO_CHANGE = "enableScreenlockAutoChange";
    private String ENABLE_AUTO_CHANGE = "enableAutoChange";
    private String TIMER_AUTO_CHANGE = "timerAutoChange";

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

    public void setEnableScreenLockAutoChange(boolean isEnabled){
        sp.edit().putBoolean(ENABLE_SCREENLOCK_AUTO_CHANGE, isEnabled).commit();
    }

    public boolean isEnabledScreenLockAutoChange(){
        return sp.getBoolean(ENABLE_SCREENLOCK_AUTO_CHANGE, true);
    }

    public void setEnableAutoChange(boolean isEnabled){
        sp.edit().putBoolean(ENABLE_AUTO_CHANGE, isEnabled).commit();
    }

    public boolean isEnabledAutoChange(){
        return sp.getBoolean(ENABLE_AUTO_CHANGE, true);
    }

    public void setTimerAutoChange(int timer){
        sp.edit().putInt(TIMER_AUTO_CHANGE, timer).commit();
    }

    public int getTimerAutoChange(){
        return sp.getInt(TIMER_AUTO_CHANGE, 4);
    }
}

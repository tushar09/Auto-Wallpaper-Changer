package club.tushar.hdwallpaper.utils;

import java.util.concurrent.TimeUnit;

import club.tushar.hdwallpaper.services.ApiServices;
import club.tushar.hdwallpaper.services.HeaderInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants{

    private static String BASE_URL = "https://api.unsplash.com/";
    private static String CLIENT_ID = "8efd42a25f5bac80716327b6e95a80b2d5a900984dd453e9ce07715be7c7c159";

    public static String order_by = "popular";
    public static String orientation = "landscape";

    public static ApiServices getApiService(){
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new HeaderInterceptor(CLIENT_ID)).build();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(5, TimeUnit.MINUTES);
        httpClient.readTimeout(5, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        httpClient.addInterceptor(new HeaderInterceptor(CLIENT_ID));  // <-- this is the important line!

        ApiServices retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);
        return retrofit;
    }

}

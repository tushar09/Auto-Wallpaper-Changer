package club.tushar.hdwallpaper.utils;

import java.util.concurrent.TimeUnit;

import club.tushar.hdwallpaper.services.ApiServices;
import club.tushar.hdwallpaper.services.HeaderInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constants{

    public static String BASE_URL = "https://pixabay.com/api/?key=13776425-388ae79a88daef057c1ae2c61";

    public static String orientation = "landscape";

    public static ApiServices getApiService(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(5, TimeUnit.MINUTES);
        httpClient.readTimeout(5, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);  // <-- this is the important line!  // <-- this is the important line!

        ApiServices retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);
        return retrofit;
    }

}

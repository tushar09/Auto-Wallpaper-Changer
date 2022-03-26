package club.tushar.hdwallpaper.services;

import java.util.List;

import club.tushar.hdwallpaper.dto.downImage.DownloadImage;
import club.tushar.hdwallpaper.dto.foxytool.WallpaperResponseDto;
import club.tushar.hdwallpaper.dto.pixels.PixelsResponse;
import club.tushar.hdwallpaper.dto.unPlash.HomeResponseDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiServices{
    @GET()
    Call<HomeResponseDto> getHome(@Url String Url);

    @GET()
    Call<List<WallpaperResponseDto>> getHome2(@Url String url, @Query("page") int page);

    @GET("photos/{id}/download")
    Call<DownloadImage> getDownloadLocation(@Path("id") String id);

    @GET("photos/random")
    Call<List<HomeResponseDto>> getrandomPhoto(@Query("count") int count, @Query("orientation") String orientation);

    @GET("search/collections")
    Call<List<HomeResponseDto>> searchPhoto(@Query("query") String term, @Query("per_page") int count);
}

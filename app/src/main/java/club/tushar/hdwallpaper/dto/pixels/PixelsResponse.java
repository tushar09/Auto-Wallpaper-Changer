package club.tushar.hdwallpaper.dto.pixels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PixelsResponse {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("next_page")
    @Expose
    private String nextPage;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public class Photo {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("photographer")
        @Expose
        private String photographer;
        @SerializedName("photographer_url")
        @Expose
        private String photographerUrl;
        @SerializedName("photographer_id")
        @Expose
        private Integer photographerId;
        @SerializedName("avg_color")
        @Expose
        private String avgColor;
        @SerializedName("src")
        @Expose
        private Src src;
        @SerializedName("liked")
        @Expose
        private Boolean liked;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPhotographer() {
            return photographer;
        }

        public void setPhotographer(String photographer) {
            this.photographer = photographer;
        }

        public String getPhotographerUrl() {
            return photographerUrl;
        }

        public void setPhotographerUrl(String photographerUrl) {
            this.photographerUrl = photographerUrl;
        }

        public Integer getPhotographerId() {
            return photographerId;
        }

        public void setPhotographerId(Integer photographerId) {
            this.photographerId = photographerId;
        }

        public String getAvgColor() {
            return avgColor;
        }

        public void setAvgColor(String avgColor) {
            this.avgColor = avgColor;
        }

        public Src getSrc() {
            return src;
        }

        public void setSrc(Src src) {
            this.src = src;
        }

        public Boolean getLiked() {
            return liked;
        }

        public void setLiked(Boolean liked) {
            this.liked = liked;
        }

    }

    public class Src {

        @SerializedName("original")
        @Expose
        private String original;
        @SerializedName("large2x")
        @Expose
        private String large2x;
        @SerializedName("large")
        @Expose
        private String large;
        @SerializedName("medium")
        @Expose
        private String medium;
        @SerializedName("small")
        @Expose
        private String small;
        @SerializedName("portrait")
        @Expose
        private String portrait;
        @SerializedName("landscape")
        @Expose
        private String landscape;
        @SerializedName("tiny")
        @Expose
        private String tiny;

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getLarge2x() {
            return large2x;
        }

        public void setLarge2x(String large2x) {
            this.large2x = large2x;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getLandscape() {
            return landscape;
        }

        public void setLandscape(String landscape) {
            this.landscape = landscape;
        }

        public String getTiny() {
            return tiny;
        }

        public void setTiny(String tiny) {
            this.tiny = tiny;
        }

    }
}

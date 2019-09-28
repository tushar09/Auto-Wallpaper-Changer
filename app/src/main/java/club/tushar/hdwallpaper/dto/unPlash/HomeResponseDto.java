package club.tushar.hdwallpaper.dto.unPlash;

import java.util.List;

public class HomeResponseDto {

    private int total;
    private List<Hits> hits;
    private int totalHits;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Hits> getHits() {
        return hits;
    }

    public void setHits(List<Hits> hits) {
        this.hits = hits;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public static class Hits {
        private String previewURL;
        private String userImageURL;
        private int previewWidth;
        private int imageSize;
        private int favorites;
        private String user;
        private int downloads;
        private String tags;
        private int previewHeight;
        private String type;
        private String webformatURL;
        private int imageHeight;
        private String pageURL;
        private int comments;
        private int views;
        private int user_id;
        private int id;
        private int imageWidth;
        private int likes;
        private int webformatWidth;
        private int webformatHeight;
        private String largeImageURL;

        public String getPreviewURL() {
            return previewURL;
        }

        public void setPreviewURL(String previewURL) {
            this.previewURL = previewURL;
        }

        public String getUserImageURL() {
            return userImageURL;
        }

        public void setUserImageURL(String userImageURL) {
            this.userImageURL = userImageURL;
        }

        public int getPreviewWidth() {
            return previewWidth;
        }

        public void setPreviewWidth(int previewWidth) {
            this.previewWidth = previewWidth;
        }

        public int getImageSize() {
            return imageSize;
        }

        public void setImageSize(int imageSize) {
            this.imageSize = imageSize;
        }

        public int getFavorites() {
            return favorites;
        }

        public void setFavorites(int favorites) {
            this.favorites = favorites;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public int getDownloads() {
            return downloads;
        }

        public void setDownloads(int downloads) {
            this.downloads = downloads;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public int getPreviewHeight() {
            return previewHeight;
        }

        public void setPreviewHeight(int previewHeight) {
            this.previewHeight = previewHeight;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWebformatURL() {
            return webformatURL;
        }

        public void setWebformatURL(String webformatURL) {
            this.webformatURL = webformatURL;
        }

        public int getImageHeight() {
            return imageHeight;
        }

        public void setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
        }

        public String getPageURL() {
            return pageURL;
        }

        public void setPageURL(String pageURL) {
            this.pageURL = pageURL;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImageWidth() {
            return imageWidth;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getWebformatWidth() {
            return webformatWidth;
        }

        public void setWebformatWidth(int webformatWidth) {
            this.webformatWidth = webformatWidth;
        }

        public int getWebformatHeight() {
            return webformatHeight;
        }

        public void setWebformatHeight(int webformatHeight) {
            this.webformatHeight = webformatHeight;
        }

        public String getLargeImageURL() {
            return largeImageURL;
        }

        public void setLargeImageURL(String largeImageURL) {
            this.largeImageURL = largeImageURL;
        }
    }
}

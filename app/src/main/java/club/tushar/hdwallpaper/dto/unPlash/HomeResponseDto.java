package club.tushar.hdwallpaper.dto.unPlash;

import java.util.List;

public class HomeResponseDto{

    private User user;
    private List<String> current_user_collections;
    private boolean liked_by_user;
    private int likes;
    private boolean sponsored;
    private List<String> categories;
    private Links links;
    private Urls urls;
    private String color;
    private int height;
    private int width;
    private String updated_at;
    private String created_at;
    private String id;

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public List<String> getCurrent_user_collections(){
        return current_user_collections;
    }

    public void setCurrent_user_collections(List<String> current_user_collections){
        this.current_user_collections = current_user_collections;
    }

    public boolean getLiked_by_user(){
        return liked_by_user;
    }

    public void setLiked_by_user(boolean liked_by_user){
        this.liked_by_user = liked_by_user;
    }

    public int getLikes(){
        return likes;
    }

    public void setLikes(int likes){
        this.likes = likes;
    }

    public boolean getSponsored(){
        return sponsored;
    }

    public void setSponsored(boolean sponsored){
        this.sponsored = sponsored;
    }

    public List<String> getCategories(){
        return categories;
    }

    public void setCategories(List<String> categories){
        this.categories = categories;
    }

    public Links getLinks(){
        return links;
    }

    public void setLinks(Links links){
        this.links = links;
    }

    public Urls getUrls(){
        return urls;
    }

    public void setUrls(Urls urls){
        this.urls = urls;
    }

    public String getColor(){
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public String getUpdated_at(){
        return updated_at;
    }

    public void setUpdated_at(String updated_at){
        this.updated_at = updated_at;
    }

    public String getCreated_at(){
        return created_at;
    }

    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public static class User{
        private int total_photos;
        private int total_likes;
        private int total_collections;
        private String instagram_username;
        private Profile_image profile_image;
        private Links links;
        private String location;
        private String bio;
        private String portfolio_url;
        private String twitter_username;
        private String last_name;
        private String first_name;
        private String name;
        private String username;
        private String updated_at;
        private String id;

        public int getTotal_photos(){
            return total_photos;
        }

        public void setTotal_photos(int total_photos){
            this.total_photos = total_photos;
        }

        public int getTotal_likes(){
            return total_likes;
        }

        public void setTotal_likes(int total_likes){
            this.total_likes = total_likes;
        }

        public int getTotal_collections(){
            return total_collections;
        }

        public void setTotal_collections(int total_collections){
            this.total_collections = total_collections;
        }

        public String getInstagram_username(){
            return instagram_username;
        }

        public void setInstagram_username(String instagram_username){
            this.instagram_username = instagram_username;
        }

        public Profile_image getProfile_image(){
            return profile_image;
        }

        public void setProfile_image(Profile_image profile_image){
            this.profile_image = profile_image;
        }

        public Links getLinks(){
            return links;
        }

        public void setLinks(Links links){
            this.links = links;
        }

        public String getLocation(){
            return location;
        }

        public void setLocation(String location){
            this.location = location;
        }

        public String getBio(){
            return bio;
        }

        public void setBio(String bio){
            this.bio = bio;
        }

        public String getPortfolio_url(){
            return portfolio_url;
        }

        public void setPortfolio_url(String portfolio_url){
            this.portfolio_url = portfolio_url;
        }

        public String getTwitter_username(){
            return twitter_username;
        }

        public void setTwitter_username(String twitter_username){
            this.twitter_username = twitter_username;
        }

        public String getLast_name(){
            return last_name;
        }

        public void setLast_name(String last_name){
            this.last_name = last_name;
        }

        public String getFirst_name(){
            return first_name;
        }

        public void setFirst_name(String first_name){
            this.first_name = first_name;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getUsername(){
            return username;
        }

        public void setUsername(String username){
            this.username = username;
        }

        public String getUpdated_at(){
            return updated_at;
        }

        public void setUpdated_at(String updated_at){
            this.updated_at = updated_at;
        }

        public String getId(){
            return id;
        }

        public void setId(String id){
            this.id = id;
        }
    }

    public static class Profile_image{
        private String large;
        private String medium;
        private String small;

        public String getLarge(){
            return large;
        }

        public void setLarge(String large){
            this.large = large;
        }

        public String getMedium(){
            return medium;
        }

        public void setMedium(String medium){
            this.medium = medium;
        }

        public String getSmall(){
            return small;
        }

        public void setSmall(String small){
            this.small = small;
        }
    }

    public static class Links{
        private String download_location;
        private String download;
        private String html;
        private String self;

        public String getDownload_location(){
            return download_location;
        }

        public void setDownload_location(String download_location){
            this.download_location = download_location;
        }

        public String getDownload(){
            return download;
        }

        public void setDownload(String download){
            this.download = download;
        }

        public String getHtml(){
            return html;
        }

        public void setHtml(String html){
            this.html = html;
        }

        public String getSelf(){
            return self;
        }

        public void setSelf(String self){
            this.self = self;
        }
    }

    public static class Urls{
        private String thumb;
        private String small;
        private String regular;
        private String full;
        private String raw;

        public String getThumb(){
            return thumb;
        }

        public void setThumb(String thumb){
            this.thumb = thumb;
        }

        public String getSmall(){
            return small;
        }

        public void setSmall(String small){
            this.small = small;
        }

        public String getRegular(){
            return regular;
        }

        public void setRegular(String regular){
            this.regular = regular;
        }

        public String getFull(){
            return full;
        }

        public void setFull(String full){
            this.full = full;
        }

        public String getRaw(){
            return raw;
        }

        public void setRaw(String raw){
            this.raw = raw;
        }
    }
}

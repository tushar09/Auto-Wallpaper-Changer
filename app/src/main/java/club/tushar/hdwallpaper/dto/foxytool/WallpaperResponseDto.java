package club.tushar.hdwallpaper.dto.foxytool;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WallpaperResponseDto {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("imageId")
    @Expose
    private String imageId;

    @SerializedName("rawUrl")
    @Expose
    private String rawUrl;

    @SerializedName("regularUrl")
    @Expose
    private String regularUrl;

    @SerializedName("camera")
    @Expose
    private Object camera;

    @SerializedName("username")
    @Expose
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    public String getRegularUrl() {
        return regularUrl;
    }

    public void setRegularUrl(String regularUrl) {
        this.regularUrl = regularUrl;
    }

    public Object getCamera() {
        return camera;
    }

    public void setCamera(Object camera) {
        this.camera = camera;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(WallpaperResponseDto.class.getName())
                .append('@')
                .append(Integer.toHexString(System.identityHashCode(this)))
                .append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("imageId");
        sb.append('=');
        sb.append(((this.imageId == null) ? "<null>" : this.imageId));
        sb.append(',');
        sb.append("rawUrl");
        sb.append('=');
        sb.append(((this.rawUrl == null) ? "<null>" : this.rawUrl));
        sb.append(',');
        sb.append("regularUrl");
        sb.append('=');
        sb.append(((this.regularUrl == null) ? "<null>" : this.regularUrl));
        sb.append(',');
        sb.append("camera");
        sb.append('=');
        sb.append(((this.camera == null) ? "<null>" : this.camera));
        sb.append(',');
        sb.append("username");
        sb.append('=');
        sb.append(((this.username == null) ? "<null>" : this.username));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}

package core.user;

import org.json.JSONObject;

public class Profile {
    private String username;
    private String bio;
    private String image;
    private Boolean following;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public JSONObject toJson() {
        return new JSONObject()
            .put("username", username)
            .put("bio", bio)
            .put("image", image == null ? JSONObject.NULL : image)
            .put("following", following);
    }
}
package core.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity(name = "Profile")
@Table(name = "USER_TABLE")
public class Profile extends AbstractUser {

    public void followedBy(User userContext) {
        this.followedBy.add(userContext);

    }

    public void unfollowedBy(User userContext) {
        this.followedBy.remove(userContext);
    }

    public JSONObject toJson(User userContext) {
        return new JSONObject()
            .put("username", username)
            .put("bio", bio == null ? JSONObject.NULL : bio)
            .put("image", image == null ? JSONObject.NULL : image)
            .put("following", (this.followedBy.contains(userContext)));
    }
}
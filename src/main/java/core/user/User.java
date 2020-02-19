package core.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.json.JSONObject;

import core.user.AbstractUser;

@Entity(name = "User")
@Table(name = "User_Table")
public class User extends AbstractUser {

    private Set<Long> following = new HashSet<Long>();

    public Set<Long> getFollowing() {
        return following;
    }

    public Boolean checkFollowing(Long celebId) {
        return following.contains(celebId);
    }

    public void follow(Long celebId) {
        following.add(celebId);
    }

    public void unfollow(Long celebId) {
        following.remove(celebId);
    }

    public JSONObject toJson() {
        return new JSONObject()
            .put("email", email)
            .put("username", username)
            .put("bio", bio == null ? JSONObject.NULL : bio)
            .put("image", image == null ? JSONObject.NULL : image);
            // JWT is provided at API
    }
}

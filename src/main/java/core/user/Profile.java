package core.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.json.JSONObject;

@Entity(name = "Profile")
@Table(name = "User_Table")
public class Profile extends AbstractUser {

    public JSONObject toJson() {
        return new JSONObject()
            .put("username", username)
            .put("bio", bio == null ? JSONObject.NULL : bio)
            .put("image", image == null ? JSONObject.NULL : image);
            // .put("following", following);
    }
}
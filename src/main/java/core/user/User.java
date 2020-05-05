package core.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.json.JSONObject;

import core.user.AbstractUser;

@Entity(name = "User")
@Table(name = "USER_TABLE")
public class User extends AbstractUser {

    public JSONObject toJson() {
        return new JSONObject()
            .put("email", email)
            .put("username", username)
            .put("bio", bio == null ? JSONObject.NULL : bio)
            .put("image", image == null ? JSONObject.NULL : image);
            // JWT is provided at API
    }
}

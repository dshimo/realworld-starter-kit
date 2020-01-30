package core.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.JSONObject;

// User contains Profile
@Entity
@Table(name = "Users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.TABLE)
    @Id
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "userPassword", nullable = false)
    private String password;

    @Column(name = "bio", nullable = true)
    private String bio;

    @Column(name = "image", nullable = true)
    private String image;

    @OneToMany
    private Set<User> following = new HashSet<User>();

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImg() {
        return image;
    }

    public void setImg(String image) {
        this.image = image;
    }

    public Boolean checkFollowing(User celeb) {
        return following.contains(celeb);
    }

    public void follow(User celeb) {
        following.add(celeb);
    }

    public void unfollow(User celeb) {
        following.remove(celeb);
    }

    public void update(String email, String username, String password, String bio, String image) {
        if (! "".equals(email)) {
            this.email = email;
        }
        if (! "".equals(username)) {
            this.username = username;
        }
        if (! "".equals(password)) {
            this.password = password;
        }
        if (! "".equals(bio)) {
            this.bio = bio;
        }
        if (! "".equals(image)) {
            this.image = image;
        }
    }

    // In case you needed to print the User to verify
    @Override
    public String toString() {
        String json = new JSONObject()
            .put("email", email)
            .put("username", username)
            .put("bio", bio == null ? JSONObject.NULL : bio)
            .put("image", image == null ? JSONObject.NULL : image)
            .toString();
        return json;
    }

    public JSONObject toJson() {
        return new JSONObject()
            .put("email", email)
            .put("username", username)
            .put("bio", bio == null ? JSONObject.NULL : bio)
            .put("image", image == null ? JSONObject.NULL : image);
            // JWT is provided at API
    }

    public JSONObject toProfileJson() {
        return new JSONObject()
            .put("username", username)
            .put("bio", bio == null ? JSONObject.NULL : bio)
            .put("image", image == null ? JSONObject.NULL : image);
            // Follow logic is applied at API
    }
}

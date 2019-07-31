package core.user;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.GenerationType;

import org.eclipse.microprofile.jwt.Claim;

@Entity
@Table(name = "Users")
@NamedQueries({
    @NamedQuery(name = "Users.findAllUsers", query = "SELECT u FROM User u"),
    @NamedQuery(name = "Users.findUser", query = "SELECT u FROM User u WHERE u.username = :username")
})
@RequestScoped
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "userID")
    private String userID;

    @Inject
    @Claim("email")
    @Column(name = "userEmail")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "bio")
    private String bio;

    @Column(name = "image")
    private String image;

    public User() {
    }

    public User(String email, String username, String password, String bio, String image) {
        this.userID = UUID.randomUUID().toString();
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }

    public String getID() {
        return userID;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User: [ID = " + userID + ", Email = " + email + ", Username = " + username + ", Bio = " + bio + ", Image = "
                + image + "]";
    }

}

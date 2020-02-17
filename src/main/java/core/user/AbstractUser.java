package core.user;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    protected String email;
    @Column(name = "username", nullable = false, unique = true)
    protected String username;
    @Column(name = "userPassword", nullable = false)
    private String password;
    @Column(name = "bio", nullable = true)
    protected String bio;
    @Column(name = "image", nullable = true)
    protected String image;

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

}
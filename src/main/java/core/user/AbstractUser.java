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
    
    public void update(String email, String username, String password, String image, String bio) {
        if (email != null && ! "".equals(email)) {
            System.out.println("Updating email");
            this.email = email;
        }
        if (username != null && ! "".equals(username)) {
            System.out.println("Updating username");
            this.username = username;
        }
        if (password != null && ! "".equals(password)) {
            System.out.println("Updating password");
            this.password = password;
        }
        if (image != null) {
            System.out.println("Update image");
            this.image = image;
        }
        if (bio != null) {
            System.out.println("Updating bio");
            this.bio = bio;
        }
    }

}
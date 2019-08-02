package core.user;

import java.io.Serializable;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.microprofile.jwt.Claim;

@Entity
@Table(name = "Users")
@RequestScoped

public class AuthUser implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
    
    @Column(name = "token")
    private String token;

    public AuthUser() {
    }

    public AuthUser(User user, String token) {
    	this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.bio = user.getBio();
        this.image = user.getImage();
        this.token = token;
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
	
	public String getToken() {
		return token;
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
        return "User: [Email = " + email + ", Username = " + username + ", Bio = " + bio + ", Image = " + image + "]";
    }
}

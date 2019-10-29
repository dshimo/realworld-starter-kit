package core.user;

import java.util.UUID;

public class Users {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class User {
        private String userID = UUID.randomUUID().toString();
        private String email;    // Required
        private String username; // Required
        private String password; // Required
        private String bio;
        private String image;

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

        @Override
        public String toString() {
            return "User: [ID = " + userID + ", Email = " + email + ", Username = " + username + ", Bio = " + bio + 
                ", Image = " + image + "]";
        }
    }
}
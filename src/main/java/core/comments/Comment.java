package core.comments;

import org.json.JSONObject;

import core.user.Profile;

public class Comment {
    private String id;
    private String createdAt;
    private String updatedAt;
    private String body;
    private Profile author;

    public String getId() {
        return id;
    }
    
    public String getCreatedAt() {
        // Set defined time here
        // updatedAt = createdAt;
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Profile getAuthor() {
        return author;
    }

    public void setAuthor(Profile author) {
        this.author = author;
    }

    public JSONObject toJson() {
        return new JSONObject()
            .put("id", id)
            .put("createdAt", createdAt)
            .put("updatedAt", updatedAt)
            .put("body", body)
            .put("author", author);
    }
}
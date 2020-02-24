package core.article;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.github.slugify.Slugify;

import org.joda.time.DateTime;
import org.json.JSONObject;

import core.user.Profile;

@MappedSuperclass
public abstract class AbstractArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "slug")
    private String slug;
    @Column(name = "title", nullable =  false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "body", nullable = false)
    private String body;
    @Column(name = "tags", nullable = true)
    public List<String> tagList;
    @Column(name = "createdAt")
    private DateTime createdAt;
    @Column(name = "updatedAt")
    private DateTime updatedAt;
    @Column(name = "favoritesCount")
    private int favoritesCount;
    @ManyToOne
    private Profile author;

    public AbstractArticle() {
        DateTime created = DateTime.now();
        this.createdAt = created;
        this.updatedAt = created;
        this.favoritesCount = 0;
    }

    public String getSlug() {
        return slug;
    }

    public void initSlug() {
        this.slug = new Slugify().slugify(this.title);
    }

    public void setSlug(String title) {
        this.slug = new Slugify().slugify(title);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTags() {
        return tagList;
    }

    public void setTags(List<String> tagList) {
        this.tagList = tagList;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = DateTime.now();
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void upFavoritesCount() {
        ++this.favoritesCount;
    }

    public void downFavoritesCount() {
        --this.favoritesCount;
    }

    public Profile getAuthor() {
        return author;
    }

    public void setAuthor(Profile author) {
        this.author = author;
    }

    public JSONObject toJson() {
        return new JSONObject()
            .put("slug", slug == null ? JSONObject.NULL : slug)
            .put("title", title)
            .put("description", description)
            .put("body", body)
            .put("tagList", tagList == null ? JSONObject.NULL : tagList)
            .put("createdAt", createdAt)
            .put("updatedAt", updatedAt)
            .put("favoritesCount", favoritesCount)
            .put("author", author == null ? JSONObject.NULL : author.toJson());
    }

}
package core.article;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "Articles")
@NamedQueries({
	@NamedQuery(name = "Articles.findByID", query = "SELECT a FROM Article a WHERE a.id = :id"),
	@NamedQuery(name = "Articles.findBySlug", query = "SELECT a FROM Article a WHERE a.slug = :slug"),
	@NamedQuery(name = "Articles.findUserFeed", query = "SELECT a FROM Article a WHERE a.userID = :userID")
})
@RequestScoped
public class Article {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
	private String id;
    
    @Column(name = "userID")
	private String userID;
    
    @Column(name = "slug")
	private String slug;
    
    @Column(name = "title")
	private String title;
    
    @Column(name = "description")
	private String description;
    
    @Column(name = "body")
	private String body;
    
//    @JoinColumn(name = "tags")
//	private List<Tag> tags;
    
    @Column(name = "createdAt")
	private String createdAt;
    
    @Column(name = "updatedAt")
	private String updatedAt;
    
    @Column(name = "favorited")
    private boolean favorited;
    
    @Column(name = "favoritesCount")
    private int favoritesCount;

    public Article() {
    	
    }
    
    public Article(String title, String description, String body, String[] tagList, String userId) {
    	this(title, description, body, tagList, userId, ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT ));
    }
	
    public Article(String title, String description, String body, String[] tagList, String userId, String createdAt) {
        this.id = UUID.randomUUID().toString();
        this.slug = toSlug(title);
        this.title = title;
        this.description = description;
        this.body = body;
        //this.tags = Arrays.stream(tagList).collect(Collectors.toSet()).stream().map(Tag::new).collect(Collectors.toList());
        this.userID = userId;
        this.createdAt = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );
        this.updatedAt = createdAt;
    }

    public void update(String title, String description, String body) {
        if (!"".equals(title)) {
            this.title = title;
            this.slug = toSlug(title);
        }
        if (!"".equals(description)) {
            this.description = description;
        }
        if (!"".equals(body)) {
            this.body = body;
        }
		this.updatedAt = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );
    }

    private String toSlug(String title) {
        return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
    }
    
    public String getID() {
    	return id;
    }

	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getUserID() {
		return userID;
	}
	
//	public List<Tag> getTagList() {
//		return tags;
//	}
	
	public String getSlug() {
		return slug;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}
	
	public String getUpdatedAt() {
		return updatedAt;
	}
	
	public String getAuthor() {
		return userID;
	}
	
	public boolean isFavorited() {
		return favorited;
	}
	
	public int getFavoritesCount() {
		return favoritesCount;
	}
}

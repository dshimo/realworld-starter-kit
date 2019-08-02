package core.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ArticleFavorites")
@NamedQuery(name = "ArticleFavorites.find", query = "SELECT aF FROM ArticleFavorite aF WHERE aF.articleID = :articleID")
public class ArticleFavorite {
	
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "articleID")
	private String articleID;
    
    @Column(name = "userID")
	private String userID;
	
	public ArticleFavorite(String articleID, String userID) {
		this.setArticleID(articleID);
		this.setUserID(userID);
	}
	
	public ArticleFavorite() {
		
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getArticleID() {
		return articleID;
	}

	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}
}

package core.article;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Article")
@Table(name = "Article_Table")
public class Article extends AbstractArticle {

}
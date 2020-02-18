package core.article;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Tag")
@Table(name = "Article_Table")
public class Tag extends AbstractArticle {

}
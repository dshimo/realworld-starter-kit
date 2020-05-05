package core.article;

// import java.util.HashSet;
// import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Article")
@Table(name = "Article_Table")
public class Article extends AbstractArticle {
    // private Set<Long> favoritedBy = new HashSet<Long>();
    
    // public void addFavorited(Long userId) {
    //     favoritedBy.add(userId);
    // }

    // public void rmFavorited(Long userId) {
    //     favoritedBy.remove(userId);
    // }

    // public boolean checkFavorited(Long userId) {
    //     return favoritedBy.contains(userId);
    // }
}
package dao;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import core.article.Article;

@RequestScoped
public class ArticleDao {

    @PersistenceContext(name = "realWorld-jpa")
    private EntityManager em;

    public void createArticle(Article article) {
        System.out.println("Creating article...");
        try {
            em.persist(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Exiting creating article...");
        
    }
}
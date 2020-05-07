package dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import core.article.Article;
import core.comments.Comment;

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

    public Article findArticle(String slug) {
        try {
            return em.createQuery("SELECT a FROM Article a WHERE a.slug = :slug", Article.class)
                    .setParameter("slug", slug).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Article updateArticle(Article article, Article updates) {
        Article dbArticle = findArticle(article.getSlug());
        if (dbArticle == null) {
            return null;
        }
        dbArticle.update(updates.getTitle(), updates.getDescription(), updates.getBody());
        return em.merge(dbArticle);
    }

    public void deleteArticle(String slug) {
        em.remove(findArticle(slug));
    }

    public List<String> getTags() {
        return em.createQuery("SELECT a.tagList FROM Article a", String.class).getResultList();
    }

    public Long createComment(String slug, Comment comment) {
        System.out.println("Creating comment...");
        try {
            em.persist(comment);
            findArticle(slug).addComment(comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("CommentID is " + comment.getId());
        System.out.println("Exiting creating comment...");
        return comment.getId();
    }

    public void deleteComment(String slug, Long id) {
        em.remove(em.find(Comment.class, id));
    }

    public Comment findComment(Long commentId) {
        try {
            return em.find(Comment.class, commentId);
        } catch (NoResultException e) {
            return null;
        }
    }
}
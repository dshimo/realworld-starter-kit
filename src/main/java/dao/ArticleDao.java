package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import core.article.Article;
import core.user.Profile;

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

    public Article findArticle(Long articleId) {
        try {
            return em.find(Article.class, articleId);
        } catch (NoResultException e) {
            return null;
        }
    }

    public Article getArticle(String slug) {
        try {
            return em.createQuery("SELECT a FROM Article a WHERE a.slug = :slug", Article.class)
                .setParameter("slug", slug)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Article updateArticle(Article article, String slug) {
        Article dbArticle = getArticle(slug);
        if (dbArticle == null) {
            return null;
        }
        return em.merge(dbArticle);
    }

    public void deleteArticle(String slug) {
        em.remove(getArticle(slug));
    }

    public List<Article> defaultListArticle(int limit, int offset) {
        List<Article> test =  em.createQuery("SELECT a FROM Article a ORDER BY a.updatedAt DESC", Article.class)
            .setMaxResults(limit)
            .getResultList();
        return test;
    }

    public List<Article> grabFeed(int limit, int offset, Set<Profile> following) {
        return em.createQuery("SELECT a FROM Article a WHERE a.id IN :ids ORDER BY a.updatedAt", Article.class)
            .setParameter("ids", following)
            .setMaxResults(limit)
            .getResultList();
    }

    public List<Article> listArticles(String tag, String author, Boolean favorited, int limit, int offset) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        StringBuilder query = new StringBuilder();
        List<String> where = new ArrayList<String>();
        query.append("SELECT a FROM Article a");

        if (tag != null) {
            where.add(" :tag MEMBER OF a.tagList ");
            parameters.put("tag", tag);
        }
        if (author != null) {
            where.add(" a.profile.username = :author ");
            parameters.put("author", author);
        }
        // if (favorited != null) {
        //     where.add(" ? ");
        //     parameters.put("favorited", favorited);
        // }
        
        query.append(" WHERE " + String.join(" AND ", where) + " ORDER BY a.updatedAt DESC");
        TypedQuery<Article> jpaQuery = em.createQuery(query.toString(), Article.class);
        for (String key  : parameters.keySet()) {
            jpaQuery.setParameter(key, parameters.get(key));
        }
        jpaQuery.setMaxResults(limit);
        return jpaQuery.getResultList();
    }

    public List<String> getTags() {
        return em.createQuery("SELECT a.tagList FROM Article a", String.class).getResultList();
    }

}
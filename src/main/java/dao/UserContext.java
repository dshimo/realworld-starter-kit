package dao;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.json.JSONObject;

import core.article.Article;
import core.user.Profile;
import core.user.User;

@RequestScoped
public class UserContext {

    @PersistenceContext(name = "realWorld-jpa")
    private EntityManager em;

    public User findUser(Long userId) {
        try {
            return (userId != null) ? em.find(User.class, userId) : null;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Profile findProfile(String username) {
        try {
            return em.createQuery("SELECT p FROM Profile p WHERE p.username = :username", Profile.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public JSONObject findProfileByUsername(Long userId, String username) {
        User currentUser = findUser(userId);
        Profile profile = findProfile(username);
        if (profile == null) return null;
        return new JSONObject().put("profile", profile.toJson(currentUser));
    }

    public void followProfile(Long userId, String username) {
        User currentUser = findUser(userId);
        Profile celeb = findProfile(username);
        if (celeb != null) {
            celeb.followedBy(currentUser);
        } else {
            return;
        }
    }

    public void unfollowProfile(Long userId, String username) {
        User currentUser = findUser(userId);
        Profile celeb = findProfile(username);
        if (celeb != null) {
            celeb.unfollowedBy(currentUser);
        } else {
            return;
        }
    }

    public JSONObject findArticleJson(Long userId, String slug) {
        User currentUser = findUser(userId);
        Article article = getArticle(slug);
        return new JSONObject().put("article", article.toJson(currentUser));
    }

    public List<JSONObject> filterArticles(Long userId, String tag, String author, String favorited, int limit, int offset) {
        User currentUser = findUser(userId);
        List<Article> articles = em.createQuery("SELECT a FROM Article a ORDER BY a.updatedAt DESC", Article.class)
                .setMaxResults(limit).getResultList();

        // If any filter is provided, we filter the list
        if (tag != null || author != null || favorited != null) {
            Profile profile = findProfile(favorited);
            // Filter in one iteration for tag, author, and favorited
            // For each parameter, accept if param is null or satisfies condition
            articles = articles.stream().filter(a -> 
                (tag == null || a.getTags().contains(tag)) && 
                (author == null || a.getAuthor().getUsername().equals(author)) && 
                (favorited == null || (profile == null ? false : profile.checkFavorited(a)))).collect(Collectors.toList());
        }
        return articles.stream()
            .map(a -> a.toJson(currentUser)).skip(offset).collect(Collectors.toList());
    }

    public List<JSONObject> grabFeed(Long userId, int limit, int offset) {
        User currentUser = findUser(userId);
        List<Article> articles = em.createQuery("SELECT a FROM Article a ORDER BY a.updatedAt DESC", Article.class)
                .setMaxResults(limit).getResultList();

        return articles.stream()
            .filter(a -> a.getAuthor().checkFollowedBy(currentUser))
            .map(a -> a.toJson(currentUser)).skip(offset).collect(Collectors.toList());
    }

    public JSONObject favoriteArticle(Long userId, String slug) {
        User currentUser = findUser(userId);
        Article article = getArticle(slug);
        if (article == null) return null;
        if (currentUser.checkFavorited(article)) {
        } else {
            currentUser.favorite(article);
            article.upFavoritesCount();
        }
        return new JSONObject().put("article", article.toJson(currentUser));
    }

    public JSONObject unfavoriteArticle(Long userId, String slug) {
        User currentUser = findUser(userId);
        Article article = getArticle(slug);
        if (article == null) return null;
        if (currentUser.checkFavorited(article)) {
            currentUser.unfavorite(article);
            article.downFavoritesCount();
        }
        return new JSONObject().put("article", article.toJson(currentUser));
    }

    private Article getArticle(String slug) {
        try {
            return em.createQuery("SELECT a FROM Article a WHERE a.slug = :slug", Article.class)
                    .setParameter("slug", slug).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
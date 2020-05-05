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
            return userId != null ? em.find(User.class, userId) : null;
        } catch (NoResultException e) {
            System.out.println("UserId returned no results for user");
            return null;
        }
    }

    public Profile findProfile(String username) {
        try {
            return em.createQuery("SELECT p FROM Profile p WHERE p.username = :username", Profile.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Profile for username not found: " + username);
            return null;
        }
    }

    public void followProfile(Long userId, String username) {
        User userContext = findUser(userId);
        Profile celeb = findProfile(username);
        if (celeb != null) {
            celeb.followedBy(userContext);
        } else {
            return;
        }
    }

    public void unfollowProfile(Long userId, String username) {
        User userContext = findUser(userId);
        Profile celeb = findProfile(username);
        if (celeb != null) {
            celeb.unfollowedBy(userContext);
        } else {
            return;
        }
    }

    public JSONObject findProfileByUsername(Long userId, String username) {
        User userContext = findUser(userId);
        Profile profile = findProfile(username);
        return new JSONObject().put("profile", profile.toJson(userContext));
    }

    public JSONObject findArticle(Long userId, String slug) {
        User userContext = findUser(userId);
        Article article = getArticle(slug);
        return new JSONObject().put("article", article.toJson(userContext));
    }

    public List<JSONObject> defaultListArticles(Long userId, int limit, int offset) {
        User userContext = findUser(userId);
        List<Article> rawArticles = em.createQuery("SELECT a FROM Article a ORDER BY a.updatedAt DESC", Article.class)
                .setMaxResults(limit).getResultList();
        return rawArticles.stream()
            .map(a -> a.toJson(userContext)).skip(offset).collect(Collectors.toList());
    }

    public List<JSONObject> sortListArticles(Long userId, String tag, String author, String favorited, int limit, int offset) {
        User userContext = findUser(userId);
        List<Article> rawArticles = em.createQuery("SELECT a FROM Article a ORDER BY a.updatedAt DESC", Article.class)
                .setMaxResults(limit).getResultList();
        if (tag != null) {
            rawArticles = rawArticles.stream().filter(a -> a.getTags().contains(tag)).collect(Collectors.toList());
        }
        if (author != null) {
            rawArticles = rawArticles.stream().filter(a -> a.getAuthor().getUsername().equals(author)).collect(Collectors.toList());
        }
        if (favorited != null) {
            Profile profile = findProfile(favorited);
            rawArticles = rawArticles.stream().filter(a -> profile == null ?  false : profile.checkFavorited(a)).collect(Collectors.toList());
        }
        return rawArticles.stream()
            .map(a -> a.toJson(userContext)).skip(offset).collect(Collectors.toList());
    }

    public List<JSONObject> grabFeed(Long userId, int limit, int offset) {
        User userContext = findUser(userId);
        List<Article> rawArticles = em.createQuery("SELECT a FROM Article a ORDER BY a.updatedAt DESC", Article.class)
                .setMaxResults(limit).getResultList();
        return rawArticles.stream()
            .filter(a -> a.getAuthor().checkFollowedBy(userContext))
            .map(a -> a.toJson(userContext)).skip(offset).collect(Collectors.toList());
    }

    public boolean isPermittedEditArticle(Long userId, Article article) {
        return article.getAuthor().getId().equals(userId);
    }

    public JSONObject favoriteArticle(Long userId, String slug) {
        User userContext = findUser(userId);
        Article article = getArticle(slug);
        if (article == null) return null;
        if (userContext.checkFavorited(article)) {

        } else {
            userContext.favorite(article);
            article.upFavoritesCount();
        }
        return new JSONObject().put("article", article.toJson(userContext));
    }

    public JSONObject unfavoriteArticle(Long userId, String slug) {
        User userContext = findUser(userId);
        Article article = getArticle(slug);
        if (article == null) return null;
        if (userContext.checkFavorited(article)) {
            userContext.unfavorite(article);
            article.downFavoritesCount();
            em.merge(article);
        }
        return new JSONObject().put("article", article.toJson(userContext));
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
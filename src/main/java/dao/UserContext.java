package dao;

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
        Article article;
        try {
            article = em.createQuery("SELECT a FROM Article a WHERE a.slug = :slug", Article.class)
                    .setParameter("slug", slug)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return new JSONObject().put("article", article.toJson(userContext));
    }

}
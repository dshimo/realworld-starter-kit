package dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.json.JSONObject;

import core.user.User;

@RequestScoped
public class UserDao {

    @PersistenceContext(name = "realWorld-jpa")
    private EntityManager em;

    public void createUser(User user) {
        System.out.println("Entering createUser");
        try {
            em.persist(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Exiting createUser");
    }

    public JSONObject buildJson(String userID) {
        User traits = em
            .createQuery("SELECT u FROM User u WHERE u.id = :userID", User.class)
            .setParameter("userID", userID)
            .getSingleResult();
        return traits.toJson();
    }

    public User findUser(String userID) {
        return em.find(User.class, userID);
    }

    // public void updateUser(User user) {
    //     em.merge(user);
    // }

    public void deleteUser(String id) {
        em.remove(em.find(User.class, id));
    }

    public boolean userExists(String username) {      // em.contains does not work
        return (Long) em.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.username = :username")
            .setParameter("username", username)
            .getSingleResult() > 0;
    }

    public User findByUsername(String username) {
        return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
            .setParameter("username", username)
            .getSingleResult();
    }

    public boolean emailExists(String email) {
        return (Long) em.createQuery("SELECT COUNT(u.id) FROM User u WHERE u.email = :email")
            .setParameter("email", email)
            .getSingleResult() > 0;
    }

    public User findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
            .setParameter("email", email)
            .getSingleResult();
    }

    public List<User> findAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class)
            .getResultList();
    }

}

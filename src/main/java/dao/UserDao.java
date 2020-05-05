package dao;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

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

    public void deleteUser(Long userId) {
        em.remove(em.find(User.class, userId));
    }

    public User updateUser(User user, Long userId) {
        User dbUser = findUser(userId);
        if (dbUser == null) {
            return null;
        }
        return em.merge(dbUser);
    }

    public User findUser(Long userID) {
        try {
            return em.find(User.class, userID);
        } catch (NoResultException e) {
            return null;
        }
    }

    public User login(String email, String password) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    public boolean userExists(String username) {
        return (Long) em.createQuery("SELECT COUNT(u.USER_ID) FROM User u WHERE u.username = :username")
                .setParameter("username", username)
                .getSingleResult() > 0;
    }

    public boolean emailExists(String email) {
        return (Long) em.createQuery("SELECT COUNT(u.USER_ID) FROM User u WHERE u.email = :email")
                .setParameter("email", email)
                .getSingleResult() > 0;
    }
}

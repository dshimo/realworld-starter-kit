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

    public User findUser(Long userID) {
        try {
            return em.find(User.class, userID);
        } catch (NoResultException e) {
            return null;
        }
    }

    public User updateUser(User user, String userId) {
        User dbUser = em.find(User.class, userId);
        // Update username for ... repeat for all necessary fields?
        dbUser.setUsername(user.getUsername());
        em.merge(user);
        return dbUser;
    }

    public void deleteUser(String id) {
        em.remove(em.find(User.class, id));
    }

    public User login(String email, String password) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    public boolean userExists(String username) {
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

    // public List<User> findAllUsers() {
    //     return em.createQuery("SELECT u FROM User u", User.class)
    //         .getResultList();
    // }

}

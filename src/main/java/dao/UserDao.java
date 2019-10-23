package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import core.user.User;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UserDao {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public void createUser(User user) {
        try {
            // System.out.println(em);
            em.persist(user);
            System.out.println("Success!");
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Exiting createUser");
    }

    public User findUser(String userID) {
        return em.find(User.class, userID);
    }

    public void updateUser(User user) {
        em.merge(user);
    }

    public void deleteUser(User user) {
        em.remove(user);
    }

    // public User findByUsername(String username) {
    //     return em.createNamedQuery("Users.findByUsername", User.class).setParameter("username", username)
    //             .getSingleResult();
    // }

    // public List<User> findAllUsers() {
    //     return em.createNamedQuery("Users.findAllUsers", User.class)
    //             .getResultList();
    // }

    // public User findByEmail(String email) {
    //     return em.createNamedQuery("Users.findUser", User.class).setParameter("email", email).getSingleResult();
    // }

}

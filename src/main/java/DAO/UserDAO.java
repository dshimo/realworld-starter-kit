package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import core.user.User;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UserDAO {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public void createUser(User user) {
        try {
            em.persist(user);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public User findUser(String userID) {
        return em.find(User.class, userID);
    }
    
    public User findByUsername(String username) {
        return em.createNamedQuery("Users.findByUsername", User.class)       	
        		 .setParameter("username", username)
        		 .getSingleResult();
    }
    
    public User findByEmail(String email) {
        return em.createNamedQuery("Users.findByEmail", User.class)       	
        		 .setParameter("email", email)
        		 .getSingleResult();
    }

    public void updateUser(User user) {
        em.merge(user);
    }

    public void deleteUser(User user) {
        em.remove(user);
    }

    public List<User> findAllUsers() {
        return em.createNamedQuery("Users.findAllUsers", User.class)
        		 .getResultList();
    }

}

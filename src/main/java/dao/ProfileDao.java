package dao;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import core.user.Profile;

@RequestScoped
public class ProfileDao {

    @PersistenceContext(name = "realWorld-jpa")
    private EntityManager em;

    public Profile findProfile(Long userId) {
        try {
            return em.find(Profile.class, userId);
        } catch (NoResultException e) {
            System.out.println("Id not found.");
            return null;
        }
    }

    public Profile getProfileByUsername(String username) {
        try {
            return em.createQuery("SELECT p FROM Profile p WHERE p.username = :username", Profile.class)
                .setParameter("username", username)
                .getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Profile not found.");
            return null;
        }
    }
}
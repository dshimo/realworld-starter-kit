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

    public Profile getProfileByUsername(String username) {
        try {
            return (Profile) em.createQuery("SELECT p FROM Profile p WHERE p.username = :username")
                .setParameter("username", username)
                .getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Profile not found.");
            return null;
        }
    }
}
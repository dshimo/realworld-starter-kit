package DAO;
//package io.openliberty;
//
//import java.io.Serializable;
//import java.util.Optional;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import io.openliberty.core.Profile;
//import io.openliberty.core.user.User;
//
//@RequestScoped
//public class ProfileDAO implements Serializable {
//	private static final long serialVersionUID = 1L;
//	
//	
//	
//	@PersistenceContext(name = "jpa-unit")
//    private EntityManager em;
//	
//	@Inject
//	private UserRelationshipQueryServiceDAO userRelationDAO;
//	
//    public Profile findByUsername(String username) {
//    	User user = em.createNamedQuery("Users.findUser", User.class)
//                 .setParameter("username", username).getSingleResult();
//    	System.out.println(user.toString());
//    	return new Profile(user.getUsername(), user.getBio(), user.getImage(), userRelationDAO.isUserFollowing(user));
//    }
//}

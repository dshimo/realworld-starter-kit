package DAO;
//package io.openliberty;
//
//import javax.enterprise.context.RequestScoped;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import io.openliberty.core.user.User;
//
//@RequestScoped
//public class UserRelationshipQueryServiceDAO {
//
//	@PersistenceContext(name = "jpa-unit")
//    private EntityManager em;
//	
//    public void createRelationship(FollowRelationship followRelationship) {
//    	em.persist(followRelationship);
//    }
//    
////    public List<FollowRelationship> findRelationship(String userID, String otherID){
////    	return em.createNamedQuery("Relationships.findRelationship", User.class)
////    			 .setParameter("userID", userID)
////    			 .getResultList();
////    }
//	
//	public boolean isUserFollowing(User user) {
//		FollowRelationship fR = em.createNamedQuery("Relationships.isUserFollowing", FollowRelationship.class)
//								  .setParameter("userID", user.getID())
//								  .getSingleResult();
//		
//		if (fR.getFriendID() != null) {
//			return true;
//		}
//		return false;
//	}
////	
////	public Set<String> followingAuthors(String userID, List<String> ids){
////		
////	}
////	
////	public List<String> followedUsers(String userID){
////		
////	}
//}

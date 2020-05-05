// package dao;

// import javax.enterprise.context.RequestScoped;
// import javax.persistence.EntityManager;
// import javax.persistence.NoResultException;
// import javax.persistence.PersistenceContext;

// import org.json.JSONObject;

// import core.user.Profile;
// import core.user.User;

// @RequestScoped
// public class ProfileDao {

//     @PersistenceContext(name = "realWorld-jpa")
//     private EntityManager em;

//     public Profile findProfile(Long userId) {
//         try {
//             return em.find(Profile.class, userId);
//         } catch (NoResultException e) {
//             System.out.println("Profile for given id not found: " + userId);
//             return null;
//         }
//     }

//     public Profile getProfileByUsername(String username) {
//         try {
//             return em.createQuery("SELECT p FROM Profile p WHERE p.username = :username", Profile.class)
//                 .setParameter("username", username)
//                 .getSingleResult();
//         } catch (NoResultException e) {
//             System.out.println("Profile for username not found: " + username);
//             return null;
//         }
//     }

//     public JSONObject getProfile(String profileUsername, Long userId) {
//         Profile profile = getProfileByUsername(profileUsername);
//         if (profile == null) {
//             return null;
//         } else if (userId == null) {
//             return profile.toJson().append("following", false);
//         }

//         boolean following;
//         try {
//             User follower = em.find(User.class, userId);
//             following = follower.checkFollowing(profile);
//         } catch (NoResultException e) {
//             following = false;
//         }
//         return new JSONObject().put("profile", profile.toJson().put("following", following));
//     }
// }
package application.rest.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.json.JSONObject;

import DAO.UserDAO;
import core.user.AuthUser;
import core.user.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

@RequestScoped
@Path("user")
public class CurrentUserAPI {

	@Inject
	private UserDAO userDAO;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response currentUser(@Context HttpServletRequest httpRequest, @Context SecurityContext sec){

		System.out.println(sec.isSecure());

		
		User user = userDAO.findByUsername(httpRequest.getHeader("Authorization"));
			
		if (user != null) {
			return Response.ok()
				       .header("Access-Control-Allow-Origin", "*")
				       .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
				       .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
					   .entity(userResponse(new AuthUser(user, httpRequest.getHeader("Authorization"))))
					   .build();
		} else {
			return Response.noContent().build();
		}

	}


	/**
	 * This method returns a specific existing/stored user in Json format
	 */

//
//	/**
//	 * This method updates a new user from the submitted data (email, username,
//	 * password, bio and image) by the user.
//	 */
//	@PUT
//	@RolesAllowed({ "admin", "user" })
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public Response updateUser(String requestBody, @Context HttpServletRequest httpRequest) {
//
//		String email = "";
//		String username = "";
//		String password = "";
//		String bio = "";
//		String image = "";
//
//		JSONObject body = new JSONObject(requestBody);
//		JSONObject userObject = body.getJSONObject("user");
//		User updateUser = userDAO.findByUsername(jwtToken.getName());
//
//		if (updateUser == null) {
//			return Response.status(Response.Status.NOT_FOUND).entity("User does not exist").build();
//		}
//
//		try {
//			String newEmail = userObject.getString("email");
//			Optional<String> optEmail = Optional.of(newEmail);
//			email = optEmail.get();
//		} catch (Exception e) {
//			email = updateUser.getEmail();
//		}
//
//		try {
//			String newUsername = userObject.getString("username");
//			Optional<String> optUsername = Optional.of(newUsername);
//			username = optUsername.get();
//		} catch (Exception e) {
//			username = updateUser.getUsername();
//		}
//
//		try {
//			String newPassword = userObject.getString("password");
//			Optional<String> optPassword = Optional.of(newPassword);
//			password = optPassword.get();
//		} catch (Exception e) {
//			password = updateUser.getPassword();
//		}
//
//		try {
//			String newBio = userObject.getString("bio");
//			Optional<String> optBio = Optional.of(newBio);
//			bio = optBio.get();
//		} catch (Exception e) {
//			bio = updateUser.getBio();
//		}
//
//		try {
//			String newImage = userObject.getString("image");
//			Optional<String> optImage = Optional.of(newImage);
//			image = optImage.get();
//		} catch (Exception e) {
//			image = updateUser.getImage();
//		}
//
//		userDAO.updateUser(updateUser);
//
//		return Response.status(Response.Status.CREATED)
//				.entity(userResponse(new AuthUser(updateUser, httpRequest.getHeader("Authorization")))).build();
//	}

	private Map<String, Object> userResponse(AuthUser userWithToken) {
		return new HashMap<String, Object>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("user", userWithToken);
			}
		};
	}

}

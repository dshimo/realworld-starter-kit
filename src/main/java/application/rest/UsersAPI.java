// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package application.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.InvalidConsumerException;
import com.ibm.websphere.security.jwt.InvalidTokenException;
import com.ibm.websphere.security.jwt.JwtException;
import com.ibm.websphere.security.jwt.KeyException;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

import org.json.JSONObject;

import DAO.UserDAO;
import core.user.AuthUser;
import core.user.User;
import security.JwtGenerator;

import java.util.HashMap;

import java.util.Map;

import javax.enterprise.context.RequestScoped;

@RequestScoped
@Path("/users")
public class UsersAPI {

    @Inject
    private UserDAO userDAO;

    private JwtGenerator jwtGenerator = new JwtGenerator();;

    /**
     * This method creates a new user from the submitted data (email, username,
     * password, bio and image) by the user.
     * 
     * @throws KeyException
     * @throws InvalidClaimException
     * @throws InvalidBuilderException
     * @throws JwtException
     * @throws InvalidConsumerException 
     * @throws InvalidTokenException 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createNewUser(@Context HttpServletRequest httpRequest, String requestBody, @Context SecurityContext securityContext)
            throws JwtException, InvalidBuilderException, InvalidClaimException, KeyException, InvalidConsumerException, InvalidTokenException {
        JSONObject obj = new JSONObject(requestBody);
        JSONObject user = obj.getJSONObject("user");
        User newUser = new User(user.getString("email"), user.getString("username"), user.getString("password"), "", "");
        userDAO.createUser(newUser);
        String jwtToken = jwtGenerator.getToken(user.getString("username")); 
        
        return Response.status(Response.Status.CREATED)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
            .header(AUTHORIZATION, "Bearer " + jwtToken)
            .entity(userResponse(new AuthUser(newUser, jwtToken)))
            .build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response loginUser(@Context HttpServletRequest request, @Context HttpServletResponse response, 
        String requestBody) throws Exception {
        JSONObject obj = new JSONObject(requestBody);
        JSONObject user = obj.getJSONObject("user");
        User loginUser = userDAO.findByEmail(user.getString("email"));
        String jwtToken = jwtGenerator.getToken(loginUser.getUsername()); 
        
        if (loginUser != null && user.getString("password").equals(loginUser.getPassword())) {
            return Response.status(Response.Status.CREATED).header("Access-Control-Allow-Origin", "*")
		                   .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
		                   .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
		                   .entity(userResponse(new AuthUser(loginUser, jwtToken)))
		                   .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
	            		   .entity("User does not exist!")
	            		   .build();
        }
    }

    private Map<String, Object> userResponse(AuthUser authUser) {
        return new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("user", authUser);
            }
        };
    }
}

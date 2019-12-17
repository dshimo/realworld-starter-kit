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

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtException;
import com.ibm.websphere.security.jwt.KeyException;

import org.json.JSONException;
import org.json.JSONObject;

import core.user.User;
import core.user.Users;

// import com.ibm.websphere.security.jwt.InvalidBuilderException;
// import com.ibm.websphere.security.jwt.InvalidClaimException;
// import com.ibm.websphere.security.jwt.JwtException;
// import com.ibm.websphere.security.jwt.KeyException;

// import org.eclipse.microprofile.jwt.JsonWebToken;

// import org.json.JSONObject;

import dao.UserDao;
import security.JwtGenerator;

@RequestScoped
@Path("/users")
public class UsersAPI {

    private JwtGenerator tknGenerator = new JwtGenerator();

    @Inject
    private UserDao userDao;

    // @Inject
    // private JsonWebToken jwtToken;

    @GET
    @Path("/test")
    public Response test() {
        return Response.ok("Working endpoint.").build();
    }

    @GET
    @Path("/deleteUser/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response deleteUser(@PathParam("id") String id) {
        userDao.deleteUser(id);
        return Response.ok("Delete request received.").build();
    }

    /* Registration */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createSimpleUser(Users users)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        System.out.println("Creating simple user.");
        User user = users.getUser();
        String username = user.getUsername();
        String email = user.getEmail();

        // Required fields
        if (username == null || email == null || user.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Username, email, or password must not be null or empty.")
                .build();
        }

        // Check duplicate username/email in database
        if (userDao.userExists(username)) {
            System.out.println("User exists!");
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Username already exists.")
                .build();
        }
        if (userDao.emailExists(email)) {
            System.out.println("Email exists!");
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Email already exists.")
                .build();
        }

        userDao.createUser(user);   // Persist

        JSONObject body = new JSONObject()
            .put("users", addToken(userDao.findUser(user.getId()).toJson(), username));

        return Response.status(Response.Status.CREATED).entity(body.toString()).build();
    }

    /* Get Current User */
    @GET
    @Path("/getUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response returnUser() {
        List<User> users = userDao.findAllUsers();
        return Response.ok(users).build();
    }

    /* Update User */
    @PUT
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser() {
        return Response.ok().build();
    }

    private JSONObject addToken(JSONObject json, String username)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        return json.put("token", tknGenerator.getToken(username));
    }

}

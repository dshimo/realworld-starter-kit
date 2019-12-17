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
import javax.json.Json;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
    @Path("/buildJSON")
    public Response test2() {
        userDao.buildJson("1");
        return Response.ok("See console.").build();
    }

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

    private JSONObject addToken(JSONObject json, String username)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        return json.put("token", tknGenerator.getToken(username));
    }

    @GET
    @Path("/getUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response returnUser() {
        List<User> users = userDao.findAllUsers();
        return Response.ok(users).build();
    }

    // @GET
    // @Path("/getUser/{username}")
    // @Produces(MediaType.APPLICATION_JSON)
    // @Transactional
    // public Response loadUserInfo(@PathParam("username") String username) {
    //     System.out.println("Received request to retrieve username: " + username);
    //     User user = userDao.loadUserInfo(username);
    //     System.out.println("User: " + user);
    //     System.out.println("Pizzacat is not real.");
    //     return Response.ok(user).build();
    // }

    @GET
    @Path("/deleteUser/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response deleteUser(@PathParam("id") String id) {
        userDao.deleteUser(id);
        return Response.ok("Delete request received.").build();
    }

    // private Json buildBody() {
    //     Json body = new Json();

    // }

    // private String produceToken(String username)
    //         throws JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
    //     return tknGenerator.getToken(username);
    // }


    /**
     * This method creates a new user from the submitted data (email, username,
     * password, bio and image) by the user.
     * 
     * @throws KeyException
     * @throws InvalidClaimException
     * @throws InvalidBuilderException
     * @throws JwtException
     */
    // @POST
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // @Transactional
    // public Response createNewUser(@Context HttpServletRequest httpRequest, String requestBody)
    //         throws JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
    //     JSONObject obj = new JSONObject(requestBody);
    //     JSONObject user = obj.getJSONObject("user");
    //     User newUser = new User(user.getString("email"), user.getString("username"), user.getString("password"), "", "");
    //     userDAO.createUser(newUser);
    //     return Response.status(Response.Status.CREATED)
    //         .header("Access-Control-Allow-Origin", "*")
    //         .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
    //         .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
    //         .entity(userResponse(new AuthUser(newUser, jg.getToken(newUser.getUsername()))))
    //         .build();
    // }

    // @POST
    // @Path("login")
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // @Transactional
    // public Response loginUser(@Context HttpServletRequest request, @Context HttpServletResponse response, 
    //     String requestBody) throws Exception {
    //     JSONObject obj = new JSONObject(requestBody);
    //     JSONObject user = obj.getJSONObject("user");
    //     User loginUser = userDAO.findByEmail(user.getString("email"));
    //     if (loginUser != null && user.getString("password").equals(loginUser.getPassword())) {
    //         return Response.status(Response.Status.CREATED).header("Access-Control-Allow-Origin", "*")
    //                 .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
    //                 .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
    //                 .entity(userResponse(new AuthUser(loginUser,
    //                 request.getHeader("authorization"))))
    //                 .build();
    //     } else {
    //         return Response.status(Response.Status.NOT_FOUND).entity("Email could not be found.")
    //                 .header("Access-Control-Allow-Origin", "*")
    //                 .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
    //                 .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build();
    //     }
    // }

    // private Map<String, Object> userResponse(AuthUser authUser) {
    //     return new HashMap<String, Object>() {
    //         private static final long serialVersionUID = 1L;
    //         {
    //             put("user", authUser);
    //         }
    //     };
    // }

    // @GET
    // @Path("/testToken")
    // @Produces(MediaType.TEXT_PLAIN)
    // public Response token() throws JwtException, InvalidBuilderException,
    // InvalidClaimException, KeyException {
    // String username = "david";
    // return Response.ok(tknGenerator.getToken(username)).build();
    // }
    
}

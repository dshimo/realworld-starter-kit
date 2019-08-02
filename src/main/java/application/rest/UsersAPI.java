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
// import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
// import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtException;
import com.ibm.websphere.security.jwt.JwtToken;
import com.ibm.websphere.security.jwt.KeyException;

// import org.eclipse.microprofile.jwt.Claims;
// import org.eclipse.microprofile.jwt.JsonWebToken;

// import java.util.HashMap;
// import java.util.Map;

import org.json.JSONObject;

import DAO.UserDAO;
import core.user.User;
import security.JwtGenerator;

import javax.enterprise.context.RequestScoped;

@RequestScoped
@Path("/users")
public class UsersAPI {

    private JwtGenerator jg = new JwtGenerator();

    @Inject
    private UserDAO userDAO;

    // @Inject
    // private JsonWebToken jwtToken;

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public Response hello() throws JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        String username = "david";  
        JwtToken jwt = jg.getToken(username);
        return Response.ok(jwt.compact()).build();
    }

    @GET
    @Path("/work")
    public Response test() {
        return Response.ok("It works!").build();
    }

    // @GET
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getUser() {

    //     User newUser = new User(email, username, password, bio, image));
    // }

    // @OPTIONS
    // @Produces(MediaType.TEXT_PLAIN)
    // public Response getSimple() {
    //     return Response.ok().header("Access-Control-Allow-Origin", "*")
    //             .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
    //             .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build();
    // }

    /**
     * This method creates a new user from the submitted data (email, username,
     * password, bio and image) by the user.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createNewUser(@Context HttpServletRequest httpRequest, String requestBody) {
        JSONObject obj = new JSONObject(requestBody);
        JSONObject user = obj.getJSONObject("user");
        User newUser = new User(user.getString("email"), user.getString("username"), user.getString("password"), "", "");
        userDAO.createUser(newUser);
        return Response.status(Response.Status.CREATED)
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
            .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
            // .entity(userResponse(new AuthUser(newUser, getToken(newUser))))
            .build();

    }

    // @OPTIONS
    // @Path("login")
    // @Produces(MediaType.TEXT_PLAIN)
    // public Response getLogin() {
    //     return Response.ok().header("Access-Control-Allow-Origin", "*")
    //             .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
    //             .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build();
    // }

    // @POST
    // @Path("login")
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // @Transactional
    // public Response loginUser(@Context HttpServletRequest request, @Context HttpServletResponse response,
    //     //     String requestBody) throws Exception {
    //     // JSONObject obj = new JSONObject(requestBody);
    //     // JSONObject user = obj.getJSONObject("user");
    //     User loginUser = userDAO.findByEmail(user.getString("email"));
    //     if (loginUser != null && user.getString("password").equals(loginUser.getPassword())) {

    //         return Response.status(Response.Status.CREATED).header("Access-Control-Allow-Origin", "*")
    //                 .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
    //                 .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
    //                 // .entity(userResponse(new AuthUser(loginUser,
    //                 // request.getHeader("authorization"))))
    //                 .build();
    //     } else {
    //         return Response.status(Response.Status.NOT_FOUND).entity("User does not exist!")
    //                 .header("Access-Control-Allow-Origin", "*")
    //                 .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
    //                 .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build();
    //     }
    // }

    // private String getToken(User loginUser) {
    //     // try {
    //     // JwtBuilder jwtBuilder = JwtBuilder.create();
    //     // jwtBuilder.subject(loginUser.getEmail()).claim(Claims.sub.toString(),
    //     // loginUser.getUsername())
    //     // .claim("upn", loginUser.getUsername()) // MP-JWT defined subject claim
    //     // .claim("customClaim", "customValue");
    //     // JwtToken goToken = jwtBuilder.buildJwt();
    //     // String jwtTokenString = goToken.compact();
    //     // return jwtTokenString;
    //     // } catch (Exception e) {
    //     // System.out.println("Something went wrong! " + e);
    //     return null;
    // }
    
}

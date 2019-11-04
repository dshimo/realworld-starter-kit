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
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.print.attribute.standard.Media;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtException;
import com.ibm.websphere.security.jwt.KeyException;

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
    @Path("/testToken")
    @Produces(MediaType.TEXT_PLAIN)
    public Response token() throws JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        String username = "david";
        return Response.ok(tknGenerator.getToken(username)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createSimpleUser(Users users) {
        User user = users.getUser();
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Request must contain a username, email, and password")
                .build();
        }
        String output = "temporary";
        System.out.println(user);
        userDao.createUser(user);   // Persist

        return Response.status(Response.Status.CREATED).entity(output).build();
    }

    @GET
    @Path("/getUser")
    @Transactional
    public Response returnUser() {
        List<User> users = userDao.findAllUsers();

        System.out.println(users);
        
        return Response.ok(users).build();
    }

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
    
}

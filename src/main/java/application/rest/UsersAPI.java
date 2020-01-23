package application.rest;

import core.user.User;
import core.user.Users;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.InvalidConsumerException;
import com.ibm.websphere.security.jwt.InvalidTokenException;
import com.ibm.websphere.security.jwt.JwtException;
import com.ibm.websphere.security.jwt.KeyException;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.json.JSONException;
import org.json.JSONObject;

import dao.UserDao;
import security.JwtGenerator;

@RequestScoped
@RolesAllowed("users")
@Path("/")
public class UsersAPI {

    private JwtGenerator tknGenerator = new JwtGenerator();

    @Inject
    private UserDao userDao;

    @Inject
    private JsonWebToken jwtToken;

    @GET
    @Path("/test")
    public Response test() {
        return Response.ok("Working endpoint.").build();
    }

    // @GET
    // @Path("/deleteUser/{id}")
    // @Produces(MediaType.TEXT_PLAIN)
    // @Transactional
    // public Response deleteUser(@PathParam("id") String id) {
    // userDao.deleteUser(id);
    // return Response.ok("Delete request received.").build();
    // }

    @GET
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response makeToken()
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        String username = "dshi";
        JSONObject json = new JSONObject();
        return Response.ok(addToken(json, username).toString()).build();

    }

    /* Registration */
    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @PermitAll
    public Response createSimpleUser(Users users)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        System.out.println("Creating simple user.");
        User user = users.getUser();
        String username = user.getUsername();
        String email = user.getEmail();

        // Required fields
        if (username == null || email == null || user.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username, email, or password must not be null or empty.").build();
        }

        // Check duplicate username/email in database
        if (userDao.userExists(username)) {
            System.out.println("User exists!");
            return Response.status(Response.Status.BAD_REQUEST).entity("Username already exists.").build();
        }
        if (userDao.emailExists(email)) {
            System.out.println("Email exists!");
            return Response.status(Response.Status.BAD_REQUEST).entity("Email already exists.").build();
        }

        userDao.createUser(user); // Persist

        JSONObject body = new JSONObject().put("user", addToken(userDao.findUser(user.getId()).toJson(), username));

        return Response.status(Response.Status.CREATED).entity(body.toString()).build();
    }

    @GET
    @Path("/user")
    @Transactional
    public Response currentUser() throws InvalidTokenException, InvalidConsumerException {
        
        String username = jwtToken.getSubject();

        // Authenticate
        // Check database
        // Retrieve user and return

        return Response.ok("Username is " + username).build();
    }

    // /* Update User */
    // @PUT
    // @Path("/user")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response updateUser() {
    //     return Response.ok().build();
    // }



    private JSONObject addToken(JSONObject json, String username)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        return json.put("token", tknGenerator.getToken(username));
    }

}

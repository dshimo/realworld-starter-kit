package application.rest;

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

import core.user.User;
import core.user.Users;
import dao.UserDao;
import security.JwtGenerator;

@RequestScoped
@Path("/")
@RolesAllowed("users")
public class UsersAPI {

    private JwtGenerator tknGenerator = new JwtGenerator();

    @Inject
    private UserDao userDao;

    @Inject
    private JsonWebToken jwtToken;

    @GET
    @Path("/token")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response makeToken()
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        String username = "It was me, Dio!";
        return Response.ok(generateToken(username, "1")).build();
    }

    /* Register */
    @POST
    @Path("/users")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createSimpleUser(Users requestBody)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        System.out.println("Creating simple user.");
        User user = requestBody.getUser();
        String username = user.getUsername();
        String email = user.getEmail();
        String userId = user.getId();

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

        JSONObject body = wrapUser(userDao.findUser(userId).toJson(), username, userId);

        return Response.status(Response.Status.CREATED).entity(body.toString()).build();
    }

    /* Login */
    @POST
    @Path("/users/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response loginUser(Users requestBody)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {

        System.out.println("Logging in");

        User loginInfo = requestBody.getUser();
        String email = loginInfo.getEmail();
        String password = loginInfo.getPassword();

        if (email == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please include both email and password.")
                    .build();
        }

        JSONObject body = userDao.login(loginInfo.getEmail(), loginInfo.getPassword()).toJson();
        if (body == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with matching email and password not found.")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(wrapUser(body, body.getString("username"), "placeholder").toString())
                .build();
    }

    /* Current User */
    @GET
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getCurrent() throws InvalidTokenException, InvalidConsumerException, JSONException, JwtException,
            InvalidBuilderException, InvalidClaimException, KeyException {

        String username = jwtToken.getSubject();
        String id = jwtToken.getClaim("id");
        JSONObject body = userDao.findUser(id).toJson();
            
        if (body == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Token provides incorrect userId").build();
        }

        return Response.status(Response.Status.ACCEPTED).entity(wrapUser(body, username, id).toString()).build();
    }

    /* Update User */
    @PUT
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Users requestBody) {
        JSONObject body = userDao.updateUser(requestBody.getUser(), jwtToken.getClaim("id")).toJson();
        return Response.status(Response.Status.CREATED).entity(body.toString()).build();
    }


    private JSONObject wrapUser(JSONObject user, String username, String userId)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        user.put("token", generateToken(username, userId));
        return new JSONObject().put("user", user);
    }

    private String generateToken(String username, String userId)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        return tknGenerator.getToken(username, userId);
    }

}

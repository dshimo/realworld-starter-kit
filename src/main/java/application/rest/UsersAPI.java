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

import application.errors.ValidationMessages;
import core.user.User;
import core.user.CreateUser;
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
    private JsonWebToken jwt;

    /* Register */
    @POST
    @Path("/users")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createUser(CreateUser requestBody)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {

        System.out.println("Creating simple user.");
        User user = requestBody.getUser();
        String username = user.getUsername();
        String email = user.getEmail();

        // Required fields
        if (username.equals("") || email.equals("") || user.getPassword().equals("")) {
            return Response.status(422)
                .entity(ValidationMessages.throwError(ValidationMessages.REGISTRATION_REQUIREMENTS_BLANK))
                .build();
        }

        // Check duplicate username/email in database
        if (userDao.userExists(username)) {
            System.out.println("User exists!");
            return Response.status(422)
                .entity(ValidationMessages.throwError(ValidationMessages.DUPLICATE_USERNAME))
                .build();
        }
        if (userDao.emailExists(email)) {
            System.out.println("Email exists!");
            return Response.status(422)
                .entity(ValidationMessages.throwError(ValidationMessages.DUPLICATE_EMAIL))
                .build();
        }

        userDao.createUser(user); // Persist
        Long userId = user.getId();

        return Response.status(Response.Status.CREATED)
            .entity(wrapUser(userDao.findUser(userId).toJson(), username, userId))
            .build();
    }

    /* Login */
    @POST
    @Path("/users/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response loginUser(CreateUser requestBody)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {

        System.out.println("Logging in");

        User loginInfo = requestBody.getUser();
        String email = loginInfo.getEmail();
        String password = loginInfo.getPassword();

        if (email.equals("") || password.equals("")) {
            return Response.status(422)
                .entity(ValidationMessages.throwError(ValidationMessages.LOGIN_REQUIREMENTS_BLANK))
                .build();
        }

        if (! userDao.emailExists(email)) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ValidationMessages.throwError(ValidationMessages.EMAIL_NOT_FOUND))
                .build();
        }

        User user = userDao.login(loginInfo.getEmail(), loginInfo.getPassword());
        JSONObject body = user.toJson();
        if (body == null) {
            return Response.status(422)
                .entity(ValidationMessages.throwError(ValidationMessages.LOGIN_FAIL))
                .build();
        }

        return Response.status(Response.Status.CREATED)
            .entity(wrapUser(body, body.getString("username"), user.getId()))
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

        String username = jwt.getSubject();
        Long id = jwt.getClaim("id");
        User jwtUser = userDao.findUser(id);

        if (jwtUser == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ValidationMessages.throwError(ValidationMessages.USER_NOT_FOUND))
                .build();
        }

        return Response.status(Response.Status.ACCEPTED)
            .entity(wrapUser(jwtUser.toJson(), username, id))
            .build();
    }

    /* Update User */
    @PUT
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(CreateUser requestBody) {
        JSONObject body = userDao.updateUser(requestBody.getUser(), jwt.getClaim("id")).toJson();
        return Response.status(Response.Status.CREATED).entity(body.toString()).build();
    }


    private String wrapUser(JSONObject user, String username, Long userId)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        user.put("token", generateToken(username, userId));
        return new JSONObject().put("user", user).toString();
    }

    private String generateToken(String username, Long userId)
            throws JSONException, JwtException, InvalidBuilderException, InvalidClaimException, KeyException {
        return tknGenerator.getToken(username, userId);
    }

}

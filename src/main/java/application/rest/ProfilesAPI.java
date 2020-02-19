package application.rest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.json.JSONObject;

import application.errors.ValidationMessages;
import core.user.Profile;
import core.user.User;
import dao.ProfileDao;
import dao.UserDao;

@RequestScoped
@Path("/profiles")
@RolesAllowed("users")
public class ProfilesAPI {

    @Inject
    private ProfileDao profileDao;

    @Inject
    private UserDao userDao;

    @Inject
    private JsonWebToken jwt;

    /* Get Profile */
    // Auth optional 
    @GET
    @Path("/{username}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getProfile(@PathParam("username") String username) {
        Profile profile = profileDao.getProfileByUsername(username);

        if (profile == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ValidationMessages.throwError(ValidationMessages.PROFILE_NOT_FOUND))
                .build();
        }

        Long profileId = profile.getId();
        JSONObject body = profile.toJson();
        if (jwt.getClaim("id") == null) {
            body.put("following", false);
        } else {
            User requestUser = userDao.findUser(jwt.getClaim("id"));
            body.put("following", requestUser.checkFollowing(profileId));
        }
        return Response.ok(new JSONObject().put("profile", body).toString()).build();
    }

    /* Follow User */
    // Auth Required
    @POST
    @Path("/{username}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response followUser(@PathParam("username") String username) {
        Profile profile = profileDao.getProfileByUsername(username);
        Long profileId = profile.getId();
        User requestUser = userDao.findUser(jwt.getClaim("id"));
        requestUser.follow(profileId);
        JSONObject body = profile.toJson();
        body.put("following", requestUser.checkFollowing(profileId));
        return Response.ok(new JSONObject().put("profile", body).toString()).build();
    }

    /* Unfollow User */
    // Auth Required
    @DELETE
    @Path("/{username}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response unfollowUser(@PathParam("username") String username) {
        Profile profile = profileDao.getProfileByUsername(username);
        Long profileId = profile.getId();
        User requestUser = userDao.findUser(jwt.getClaim("id"));
        requestUser.unfollow(profileId);
        JSONObject body = profile.toJson();
        body.put("following", requestUser.checkFollowing(profileId));
        return Response.ok(new JSONObject().put("profile", body).toString()).build();
    }

}
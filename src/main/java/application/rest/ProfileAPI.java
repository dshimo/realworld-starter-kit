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

import core.user.User;
import dao.UserDao;

@RequestScoped
@Path("/profiles")
@RolesAllowed("users")
public class ProfileAPI {

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
        User profile = userDao.findByUsername(username);
        JSONObject body = profile.toProfileJson();
        if (jwt == null) {
            body.put("following", false);
        } else {
            User requestUser = userDao.findUser(jwt.getClaim("id"));
            body.put("following", requestUser.checkFollowing(profile));
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
        User profile = userDao.findByUsername(username);
        User requestUser = userDao.findUser(jwt.getClaim("id"));
        requestUser.follow(profile);
        JSONObject body = profile.toProfileJson();
        body.put("following", requestUser.checkFollowing(profile));
        return Response.ok(new JSONObject().put("profile", body).toString()).build();
    }

    /* Unfollow User */
    // Auth Required
    @DELETE
    @Path("/{username}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response unfollowUser(@PathParam("username") String username) {
        User profile = userDao.findByUsername(username);

        return Response.ok().build();
    }

}
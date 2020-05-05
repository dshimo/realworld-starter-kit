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
import dao.UserContext;

@RequestScoped
@Path("/profiles")
@RolesAllowed("users")
public class ProfilesAPI {

    @Inject
    private UserContext uc;

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
        return wrapResponse(jwt.getClaim("id"), username);
    }

    /* Follow User */
    // Auth Required
    @POST
    @Path("/{username}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response followUser(@PathParam("username") String username) {
        Long userId = jwt.getClaim("id");
        uc.followProfile(userId, username);
        return wrapResponse(userId, username);
    }

    /* Unfollow User */
    // Auth Required
    @DELETE
    @Path("/{username}/follow")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response unfollowUser(@PathParam("username") String username) {
        Long userId = jwt.getClaim("id");
        uc.unfollowProfile(userId, username);
        return wrapResponse(userId, username);
    }

    private Response wrapResponse(Long id, String username) {
        JSONObject responseBody = uc.findProfileByUsername(id, username);
        if (responseBody == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ValidationMessages.throwError(ValidationMessages.PROFILE_NOT_FOUND)).build();
        } else {
            return Response.ok(responseBody.toString()).build();
        }
    }

}
package application.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/profiles")
public class ProfileAPI {

    // TODO: All returns a Profile

    // @Inject
    // private UserDao userDao;

    /* Get Profile */
    // Auth optional 
    @GET
    @Path("/{username}")
    public Response getProfile(@PathParam("username") String username) {
        // TODO: Make a Profile table and use User as foreign keys and combine
        return Response.ok("Get Profile").build();
    }

    /* Follow User */
    // Auth Required
    @POST
    @Path("/{username}/follow")
    public Response followUser(@PathParam("username") String username) {
        return Response.ok().build();
    }

    /* Unfollow User */
    // Auth Required
    @DELETE
    @Path("/{username}/follow")
    public Response unfollowUser(@PathParam("username") String username) {
        return Response.ok().build();
    }

}
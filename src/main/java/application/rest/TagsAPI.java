package application.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/tags")
public class TagsAPI {

    @GET
    public Response getTags() {
        return Response.ok().build();
    }
}
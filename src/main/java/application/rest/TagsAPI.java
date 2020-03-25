package application.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.ArticleDao;

@RequestScoped
@Path("/tags")
public class TagsAPI {

    @Inject
    private ArticleDao articleDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTags() {
        return Response.ok(articleDao.getTags()).build();
    }
}
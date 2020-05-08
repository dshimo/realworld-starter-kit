package application.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import dao.ArticleDao;

@RequestScoped
@Path("/tags")
public class TagsAPI {

    @Inject
    private ArticleDao articleDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTags() {
        List<String> tagListList = articleDao.getTags();
        // Make set
        return Response.ok(new JSONObject().put("tags", tagListList).toString()).build();
    }
}
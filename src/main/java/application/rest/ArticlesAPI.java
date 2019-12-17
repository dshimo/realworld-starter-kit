package application.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/articles")
public class ArticlesAPI {

    /* List Articles */
    // Tag, Author, or Favorited query parameter
    @GET
    public Response listArticles() {
        return Response.ok().build();
    }

    /* Feed Articles */
    // Take limit and offset query parameters
    // Authentication required, will return multiple articles created by followed
    // users, ordered by most recent first.
    @GET
    @Path("/feed")
    public Response feedArticles() {
        return Response.ok().build();
    }

    /* Get Article */
    @GET
    @Path("/{slug}")
    public Response getArticle(@PathParam("slug") String slug) {
        return Response.ok().build();
    }

    /* Create Article */
    @POST
    public Response createArticle() {
        return Response.ok().build();
    }

    /* Update Article */
    @PUT
    @Path("/{slug}")
    public Response updateArticle(@PathParam("slug") String slug) {
        return Response.ok().build();
    }

    /* Delete Article */
    @DELETE
    @Path("/{slug}")
    public Response deleteArticle(@PathParam("slug") String slug) {
        return Response.ok().build();
    }

    /* Add Comments to an Article */
    @POST
    @Path("/{slug}/comments")
    public Response addComment(@PathParam("slug") String slug) {
        return Response.ok().build();
    }

    /* Get Comments to an Article */
    @GET
    @Path("/{slug}/comments")
    public Response getComments(@PathParam("slug") String slug) {
        return Response.ok().build();
    }

    /* Delete Comments to an Article */
    @DELETE
    @Path("/{slug}/comments/{id}")
    public Response deleteComment(@PathParam("slug") String slug, @PathParam("id") String id) {
        return Response.ok().build();
    }

    /* Favorite Article */
    @POST
    @Path("/{slug}/favorite")
    public Response favorite(@PathParam("slug") String slug) {
        return Response.ok().build();
    }

    /* Unfavorite Article */
    @DELETE
    @Path("/{slug}/favorite")
    public Response unfavorite(@PathParam("slug") String slug) {
        return Response.ok().build();
    }
}
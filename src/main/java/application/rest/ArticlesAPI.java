package application.rest;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;

import application.errors.ValidationMessages;
import core.article.Article;
import core.article.CreateArticle;
import dao.ArticleDao;
import dao.UserDao;

@RequestScoped
@Path("/articles")
@RolesAllowed("users")
public class ArticlesAPI {

    @Inject
    private ArticleDao articleDao;

    @Inject
    private UserDao userDao;

    @Inject
    private JsonWebToken jwt;

    /* List Articles */
    // Tag, Author, or Favorited query parameter
    @GET
    @PermitAll
    public Response listArticles(
            @QueryParam("tag") String tag, 
            @QueryParam("author") String author, 
            @QueryParam("favorited") Boolean favorited,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset
    ) {

        System.out.println("tag: " + tag);
        System.out.println("author: " + author);
        System.out.println("favorited: " + favorited);
        System.out.println("limit: " + limit);
        System.out.println("offset: " + offset);

        return Response.ok().build();
    }

    /* Feed Articles */
    // Take limit and offset query parameters
    // Authentication required, will return multiple articles created by followed
    // users, ordered by most recent first.
    @GET
    @Path("/feed")
    public Response feedArticles(            
            @QueryParam("limit") @DefaultValue("20") int limit, 
            @QueryParam("offset") @DefaultValue("0") int offset
    ) {
        // Set<Long> following = userDao.findUser(jwt.getClaim("id")).getFollowList();

        // How will you retrieve articles made by following?

        // 1) Retrieve all articles by all following and sort by date


        return Response.ok().build();
    }

    /* Get Article */
    @GET
    @Path("/{slug}")
    public Response getArticle(
            @PathParam("slug") String slug,
            @QueryParam("limit") @DefaultValue("20") int limit, 
            @QueryParam("offset") @DefaultValue("0") int offset
    ) {
        return Response.ok().build();
    }

    /* Create Article */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createArticle(CreateArticle requestBody) {
        Article article = requestBody.getArticle();
        String title = article.getTitle();
        String description = article.getDescription();
        String body = article.getBody();
        
        // Required fields
        if (title.equals("") || description.equals("") || body.equals("")) {
            return Response.status(422)
                .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_REQUIREMENTS_BLANK))
                .build();
        }

        articleDao.createArticle(article);

        return Response.status(Response.Status.CREATED)
            .entity(article.toJson().toString())
            .build();
    }

    /* Update Article */
    @PUT
    @Path("/{slug}")
    public Response updateArticle(            
            @PathParam("slug") String slug,
            @QueryParam("title") String title, 
            @QueryParam("description") String description,
            @QueryParam("body") Boolean body
    ) {
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
package application.rest;

import java.util.List;

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
import org.json.JSONObject;

import application.errors.ValidationMessages;
import core.article.Article;
import core.article.CreateArticle;
import core.user.User;
import dao.ArticleDao;
import dao.UserContext;

@RequestScoped
@Path("/articles")
@RolesAllowed("users")
public class ArticlesAPI {

    @Inject
    private UserContext uc;

    @Inject
    private ArticleDao articleDao;

    @Inject
    private JsonWebToken jwt;

    /* List Articles */
    // Tag, Author, or Favorited query parameter
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response listArticles(
            @QueryParam("tag") String tag, 
            @QueryParam("author") String author, 
            @QueryParam("favorited") Boolean favorited,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset
    ) {

        // User requestUser = null;
        // if (jwt.getClaim("id") != null) {
        //     requestUser = userDao.findUser(jwt.getClaim("id"));
        // }
        User userContext = uc.findUser(jwt.getClaim("id"));
        if (tag == null && author == null && favorited == null) {
            List<Article> articles = articleDao.defaultListArticle(limit, offset);
            JSONObject body = new JSONObject().put("articles", articles).put("articlesCount", articles.size());
            return Response.ok()
                .entity(body.toString())
                .build();
        }

        List<Article> articles = articleDao.listArticles(tag, author, favorited, limit, offset);
        JSONObject body = new JSONObject().put("articles", articles).put("articlesCount", articles.size());
        return Response.ok(body.toString()).build();
    }

    /* Feed Articles */
    // Take limit and offset query parameters
    // Authentication required, will return multiple articles created by followed
    // users, ordered by most recent first.
    @GET
    @Path("/feed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response feedArticles(            
            @QueryParam("limit") @DefaultValue("20") int limit, 
            @QueryParam("offset") @DefaultValue("0") int offset
    ) {
        // User userContext = userDao.findUser(jwt.getClaim("id"));

        // if (userContext == null) {
        //     return Response.status(Response.Status.NOT_FOUND)
        //         .entity(ValidationMessages.throwError(ValidationMessages.USER_NOT_FOUND))
        //         .build();
        // }
        
        // Set<Profile> following = userContext.getFollowing();
        // // Set<Long> following = user.getFollowing();
        // if (following.isEmpty()) {
        //     System.out.println("Empty follow list");
        //     return Response.ok(new JSONObject().put("articles", new int[0]).put("articlesCount", 0).toString())
        //         .build();
        // }

        // List<Article> feed = articleDao.grabFeed(limit, offset, following);

        // return Response.ok(new JSONObject().put("articles", feed).put("articlesCount", feed.size()).toString()).build();
            return Response.ok().build();
    }

    /* Get Article */
    @GET
    @Path("/{slug}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response getArticle(
            @PathParam("slug") String slug,
            @QueryParam("limit") @DefaultValue("20") int limit, 
            @QueryParam("offset") @DefaultValue("0") int offset
    ) {
        JSONObject article = uc.findArticle(jwt.getClaim("id"), slug);
        return wrapResponseArticle(article);
    }

    /* Create Article */
    // 1.0 Rework following logic probably
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createArticle(CreateArticle requestBody) {
        Article article = requestBody.getArticle();
        String title = article.getTitle();
        String description = article.getDescription();
        String articleBody = article.getBody();
        
        // Required fields
        if (title.equals("") || description.equals("") || articleBody.equals("")) {
            return Response.status(422)
                .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_REQUIREMENTS_BLANK))
                .build();
        }
        
        User userContext = uc.findUser(jwt.getClaim("id"));
        if (userContext == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ValidationMessages.throwError(ValidationMessages.USER_NOT_FOUND))
                .build();
        }

        article.initSlug();
        article.setAuthor(uc.findProfile(userContext.getUsername())); // 2nd DB read
        articleDao.createArticle(article);

        JSONObject responseBody = article.toJson((User)userContext);
        return Response.status(Response.Status.CREATED)
            .entity(new JSONObject().put("article", responseBody).toString())
            .build();
    }

    /* Update Article */
    @PUT
    @Path("/{slug}")
    public Response updateArticle(@PathParam("slug") String slug, CreateArticle requestBody) {
        Article body = articleDao.updateArticle(requestBody.getArticle(), slug);

        if (body == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_NOT_FOUND))
                .build();
        }
        
        return Response.status(Response.Status.CREATED)
            .entity(new JSONObject().put("article", body).toString())
            .build();
    }

    /* Delete Article */
    @DELETE
    @Path("/{slug}")
    public Response deleteArticle(@PathParam("slug") String slug) {
        articleDao.deleteArticle(slug);
        return Response.ok().build();
    }

    /* Add Comments to an Article */
    @POST
    @Path("/{slug}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
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

    private Response wrapResponseArticle(JSONObject responseBody) {
        if (responseBody == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_NOT_FOUND)).build();
        }
        return Response.ok(responseBody.toString()).build();
    }
}
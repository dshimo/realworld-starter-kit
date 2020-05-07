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
import core.comments.CreateComment;
import core.comments.Comment;
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
    @Transactional
    @PermitAll
    public Response listArticles(
            @QueryParam("tag") String tag, 
            @QueryParam("author") String author, 
            @QueryParam("favorited") String favoritedBy,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset
    ) {
        Long userId = jwt.getClaim("id");
        List<JSONObject> articles;
        if (tag == null && author == null && favoritedBy == null) {
            articles = uc.defaultListArticles(userId, limit, offset); // Maybe redundant at this point
        } else {
            articles = uc.sortListArticles(userId, tag, author, favoritedBy, limit, offset);
        }
        return wrapResponseArticles(articles);
    }

    /* Feed Articles */
    // Take limit and offset query parameters
    // Authentication required, will return multiple articles created by followed
    // users, ordered by most recent first.
    @GET
    @Path("/feed")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response feedArticles(            
            @QueryParam("limit") @DefaultValue("20") int limit, 
            @QueryParam("offset") @DefaultValue("0") int offset
    ) {
        Long userId = jwt.getClaim("id");
        List<JSONObject> articles = uc.grabFeed(userId, limit, offset);
        return wrapResponseArticles(articles);
    }

    /* Get Article */
    @GET
    @Path("/{slug}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
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
        if (articleDao.findArticle(article.getSlug()) != null) {
            return Response.status(422)
                .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_SLUG_EXISTS))
                .build();
        }

        article.setAuthor(uc.findProfile(userContext.getUsername())); // 2nd DB read
        articleDao.createArticle(article);

        JSONObject responseBody = article.toJson(userContext);
        return Response.status(Response.Status.CREATED)
            .entity(new JSONObject().put("article", responseBody).toString())
            .build();
    }

    /* Update Article */
    @PUT
    @Path("/{slug}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateArticle(@PathParam("slug") String slug, CreateArticle requestBody) {
        Article article = articleDao.findArticle(slug);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_NOT_FOUND))
                .build();
        }
        // 403 not permitted
        if (!uc.isPermittedEditArticle(jwt.getClaim("id"), article)) {
            return Response.status(403)
                .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_NOT_YOURS))
                .build();
        }
        Article newArticle = articleDao.updateArticle(article, requestBody.getArticle());
        JSONObject responseBody = uc.findArticle(jwt.getClaim("id"), newArticle.getSlug());
        return wrapResponseArticle(responseBody);
    }

    /* Delete Article */
    @DELETE
    @Path("/{slug}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteArticle(@PathParam("slug") String slug) {
        Article article = articleDao.findArticle(slug);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_NOT_FOUND)).build();
        }
        // 403 not permitted
        if (!uc.isPermittedEditArticle(jwt.getClaim("id"), article)) {
            return Response.status(403).entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_NOT_YOURS))
                    .build();
        }
        articleDao.deleteArticle(slug);
        return Response.ok().build();
    }

    /* Add Comments to an Article */
    @POST
    @Path("/{slug}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addComment(@PathParam("slug") String slug, CreateComment createComment) {
        Comment comment = createComment.getComment();
        // Required fields
        if (comment.getBody() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ValidationMessages.throwError(ValidationMessages.COMMENT_REQUIREMENTS_BLANK)).build();
        }

        User userContext = uc.findUser(jwt.getClaim("id"));
        if (userContext == null) {
            return Response.status(Response.Status.NOT_FOUND)
            .entity(ValidationMessages.throwError(ValidationMessages.USER_NOT_FOUND))
            .build();
        }
        comment.setAuthor(uc.findProfile(userContext.getUsername()));
        Long commentId = articleDao.createComment(slug, comment);
        JSONObject responseBody = articleDao.findComment(commentId).toJson(userContext);
        return Response.status(Response.Status.CREATED)
            .entity(new JSONObject().put("comment", responseBody).toString())
            .build();
    }

    /* Get Comments to an Article */
    @GET
    @Path("/{slug}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments(@PathParam("slug") String slug) {
        Article article = articleDao.findArticle(slug);
        List<Comment> comments = article.getComments();
        return Response.ok(new JSONObject().put("comments", comments).toString()).build();
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
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response favorite(@PathParam("slug") String slug) {
        return wrapResponseArticle(uc.favoriteArticle(jwt.getClaim("id"), slug));

    }

    /* Unfavorite Article */
    @DELETE
    @Path("/{slug}/favorite")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response unfavorite(@PathParam("slug") String slug) {
        return wrapResponseArticle(uc.unfavoriteArticle(jwt.getClaim("id"), slug));
    }

    private Response wrapResponseArticle(JSONObject responseBody) {
        if (responseBody == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ValidationMessages.throwError(ValidationMessages.ARTICLE_NOT_FOUND)).build();
        }
        return Response.ok(responseBody.toString()).build();
    }

    private Response wrapResponseArticles(List<JSONObject> articles) {
        JSONObject responseBody = new JSONObject().put("articles", articles).put("articlesCount", articles.size());
        return Response.ok(responseBody.toString()).build();
    }
}
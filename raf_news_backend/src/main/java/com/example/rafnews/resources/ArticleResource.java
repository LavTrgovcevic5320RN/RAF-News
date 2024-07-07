package com.example.rafnews.resources;

import com.example.rafnews.entities.Article;
import com.example.rafnews.entities.Category;
import com.example.rafnews.services.ArticleService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/article")
public class ArticleResource {

    @Inject
    private ArticleService articleService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allArticles() {
        return Response.ok(this.articleService.allArticles()).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Article addArticle(@Valid Article article) {
        return this.articleService.addArticle(article);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Article findArticle(@PathParam("id") Integer id) {
        return this.articleService.findArticle(id);
    }

    @DELETE
    @Path("/delete/{id}")
    public void deleteArticle(@PathParam("id") Integer id) {
        this.articleService.deleteArticle(id);
    }

    @GET
    @Path("/page/{offset}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticlePAge(@PathParam("offset") Integer offset) {
        Map<String, Object> map = new HashMap<>();
        Integer sz = this.articleService.allArticles().size();

        map.put("size", sz);
        map.put("content", this.articleService.getArticlePage(offset));

        return Response.ok(map).build();
    }

    @PATCH
    @Path("/view/{articleId}")
    public void incrementArticleViewCount(@PathParam("articleId") Integer articleId){
        this.articleService.incrementArticleViewCount(articleId);
    }

    @GET
    @Path("/popular")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMostViewedArticles(){
        return Response.ok(this.articleService.getMostViewedArticles()).build();
    }

    @GET
    @Path("/category/{categoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticlesByCategory(@PathParam("categoryId") Integer categoryId) {
        return Response.ok(this.articleService.getArticlesByCategory(categoryId)).build();
    }

    @GET
    @Path("/tag/{tagId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticlesByTag(@PathParam("tagId") Integer tagId) {
        return Response.ok(this.articleService.getArticlesByTag(tagId)).build();
    }
}

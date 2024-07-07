package com.example.rafnews.resources;

import com.example.rafnews.entities.Comment;
import com.example.rafnews.services.CommentService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/comment")
public class CommentResource {

    @Inject
    private CommentService commentService;

    @GET
    @Path("/article/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allCommentsFromArticle(@PathParam("id") Integer id) {
        return Response.ok(this.commentService.allCommentsFromArticle(id)).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Comment addComment(@Valid Comment comment) {
        return this.commentService.addComment(comment);
    }
}

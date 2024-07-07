package com.example.rafnews.resources;

import com.example.rafnews.entities.Tag;
import com.example.rafnews.services.TagService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tag")
public class TagResource {

    @Inject
    private TagService tagService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allTags() {
        return Response.ok(this.tagService.allTags()).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Tag addtag(@Valid Tag tag) {
        return this.tagService.addTag(tag);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Tag findtag(@PathParam("id") Integer id) {
        return this.tagService.findTag(id);
    }

    @GET
    @Path("/article/{articleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTagsByArticleId(@PathParam("articleId") Integer articleId) {
        return Response.ok(this.tagService.getTagsByArticleId(articleId)).build();
    }
}

package com.example.rafnews.resources;

import com.example.rafnews.entities.Category;
import com.example.rafnews.services.CategoryService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/category")
public class CategoryResource {

    @Inject
    private CategoryService categoryService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allCategories() {
        return Response.ok(this.categoryService.allCategories()).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Category addCategory(@Valid Category category) {
        return this.categoryService.addCategory(category);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category findCategory(@PathParam("id") Integer id) {
        return this.categoryService.findCategory(id);
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@PathParam("id") Integer id) {
        boolean flag = this.categoryService.deleteCategory(id);
        System.out.println("bruh " + flag);
        if(flag){
            return Response.ok("Success").build();
        }

        return Response.status(401).build();
    }

    @GET
    @Path("/page/{offset}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoryPage(@PathParam("offset") Integer offset) {
        Map<String, Object> map = new HashMap<>();
        Integer sz = this.categoryService.allCategories().size();

        map.put("size", sz);
        map.put("content", this.categoryService.getCategoryPage(offset));

        return Response.ok(map).build();
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategory(@Valid Category category) {
        System.out.println(category);

        Category newCategory = this.categoryService.updateCategory(category);

        if(newCategory != null){
            return Response.ok(newCategory).build();
        }else {
            return Response.status(500).build();
        }
    }
}

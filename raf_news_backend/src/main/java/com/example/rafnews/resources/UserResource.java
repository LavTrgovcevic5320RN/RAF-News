package com.example.rafnews.resources;

import com.example.rafnews.entities.User;
import com.example.rafnews.requests.LoginRequest;
import com.example.rafnews.requests.UpdateStatusRequest;
import com.example.rafnews.services.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/user")
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allUsers() {
        return Response.ok(this.userService.allUsers()).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public User addUser(@Valid User user) {
        return this.userService.addUser(user);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User findUser(@PathParam("id") Integer id) {
        return this.userService.findUser(id);
    }

    @DELETE
    @Path("/delete/{id}")
    public void deleteUser(@PathParam("id") Integer id) {
        this.userService.deleteUser(id);
    }

    @POST
    @Path("/login")
    @Produces({MediaType.APPLICATION_JSON})
    public Response login(@Valid LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();

        String jwt = this.userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if(jwt == null) {
            response.put("message", "Wrong credentials!");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }else if (jwt.equals("Inactive")){
            response.put("message", "Inactive user! Please contact the admin.");
            return Response.status(422, "Unprocessable Entity").entity(response).build();
        }

        response.put("jwt", jwt);

        return Response
                .ok(response)
                .header("Access-Control-Allow-Origin", "http://localhost:5173/")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("origin", "*")
                .build();
    }

    @GET
    @Path("/page/{offset}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPage(@PathParam("offset") Integer offset) {
        Map<String, Object> map = new HashMap<>();
        Integer sz = this.userService.allUsers().size();

        map.put("size", sz);
        map.put("content", this.userService.getUserPage(offset));

        return Response.ok(map).build();
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@Valid User user){
        User newUser = this.userService.updateUser(user);

        if(newUser != null){
            return Response.ok(newUser).build();
        }else {
            return Response.status(500).build();
        }
    }

    @POST
    @Path("/update/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean updateUserStatus(@PathParam("id") Integer id, @Valid UpdateStatusRequest status) {
        return this.userService.updateUserStatus(id, status.getStatus());
    }
}

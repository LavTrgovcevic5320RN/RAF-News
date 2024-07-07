package com.example.rafnews.filters;

import com.example.rafnews.resources.*;
import com.example.rafnews.services.UserService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    UserService userService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if(!this.isAuthRequired(requestContext)){
            return;
        }

        try {
            String token = requestContext.getHeaderString("Authorization");

            if(token != null && token.startsWith("Bearer ")){
                token = token.replace("Bearer ", "");
            }

            if(!this.userService.isAuthorized(token, requestContext.getUriInfo().getPath())) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }catch(Exception e){
            requestContext.abortWith(Response.status((Response.Status.UNAUTHORIZED)).build());
        }
    }

    private boolean isAuthRequired(ContainerRequestContext requestContext) {
        if(requestContext.getUriInfo().getPath().contains("login")) {
            return false;
        }

        List<Object> matchedResources = requestContext.getUriInfo().getMatchedResources();
        for(Object mr: matchedResources) {
            if(mr instanceof ArticleResource || mr instanceof CategoryResource || mr instanceof CommentResource || mr instanceof TagResource || mr instanceof UserResource){
                return true;
            }
        }

        return false;
    }
}

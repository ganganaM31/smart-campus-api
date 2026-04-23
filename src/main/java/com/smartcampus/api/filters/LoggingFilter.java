package com.smartcampus.api.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getRequestUri().toString();
        System.out.println(">>> Incoming Request - Method: " + method + ", URI: " + uri);
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext, 
                      ContainerResponseContext responseContext) {
        int status = responseContext.getStatus();
        System.out.println("<<< Outgoing Response - Status: " + status);
    }
}
package com.shivanthah.jetty.rest;

import io.swagger.jaxrs.listing.ApiListingResource;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ServerConfig extends ResourceConfig {

    public ServerConfig() {
        packages(ApiListingResource.class.getPackage().getName() );
    }
}

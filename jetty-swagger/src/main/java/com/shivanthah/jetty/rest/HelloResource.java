package com.shivanthah.jetty.rest;


import io.swagger.annotations.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@Path("/")
@Api(value = "/", description = "Web Services to browse entities",authorizations = {@Authorization(value = "basicAuth")})
public class HelloResource {

    @RolesAllowed("ADMIN")
    @GET
    @Path("/hello/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Return one entity", notes = "Returns one entity at random", response = Greeting.class
            )
    @ApiResponses(value = {@ApiResponse(code = 200,
            response = Greeting.class,
            message = "Successful operation"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 422, message = "Invalid data", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)})

    public Greeting hello(@PathParam("param") String name) {
        return new Greeting(name);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Return one entity", notes = "Returns one entity at random")
    public String helloUsingJson(Greeting greeting) {
        return greeting.getMessage() + "\n";
    }
}
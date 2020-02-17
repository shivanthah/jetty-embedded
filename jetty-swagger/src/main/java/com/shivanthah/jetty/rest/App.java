package com.shivanthah.jetty.rest;

import com.shivanthah.jetty.rest.auth.ExampleRequestFilterProvider;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.auth.SecuritySchemeDefinition;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static final int SERVER_PORT = 8081;


    public static void main(String[] args) {
        try {
            // Workaround for resources from JAR files
            Resource.setDefaultUseCaches( false );

            // Build the Swagger Bean.
            buildSwagger();

            // Holds handlers
            final HandlerList handlers = new HandlerList();

            // Handler for Swagger UI, static handler.
            handlers.addHandler( buildSwaggerUI() );

            // Handler for Entity Browser and Swagger
            handlers.addHandler( buildContext() );
            handlers.addHandler( buildContext1() );
            // Start server
            Server server = new Server( SERVER_PORT );
            server.setHandler( handlers );
            server.start();
            server.join();
        } catch ( Exception e ) {
            LOG.error( "There was an error starting up the Entity Browser", e );
        }
    }


    private static void buildSwagger()
    {
        // This configures Swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion( "1.0.0" );
        beanConfig.setResourcePackage( HelloResource.class.getPackage().getName() );
        beanConfig.setScan( true );
        beanConfig.setHost("http://localhost:"+SERVER_PORT);
        beanConfig.setContact("https://github.com/apache/incubator-pinot");
        beanConfig.setBasePath( "/" );
        beanConfig.setPrettyPrint(true);
        beanConfig.setDescription( "Entity Browser API to demonstrate Swagger with Jersey2 in an "
                + "embedded Jetty instance, with no web.xml or Spring MVC." );
        beanConfig.setTitle( "Entity Browser" );
        beanConfig.setContact("Sylvain Bugat");
        beanConfig.setLicense("Apache 2.0");
        BasicAuthDefinition securityDefinition = new BasicAuthDefinition();
        securityDefinition.setType("basic");
        Swagger swagger = beanConfig.getSwagger();
        swagger.addSecurityDefinition("basicAuth", new BasicAuthDefinition());
        beanConfig.configure(swagger);
       // beanConfig.getSwagger().addSecurityDefinition("basicAuth", securityDefinition);

    }


    private static ContextHandler buildContext()
    {
        ServerConfig resourceConfig = new ServerConfig();
        ServletContainer servletContainer = new ServletContainer( resourceConfig );
        ServletHolder entityBrowser = new ServletHolder( servletContainer );
        ServletContextHandler entityBrowserContext = new ServletContextHandler( ServletContextHandler.SESSIONS );
        entityBrowserContext.setContextPath( "/" );
        entityBrowserContext.addServlet( entityBrowser, "/*" );

        // Add the filter, and then use the provided FilterHolder to configure it
        FilterHolder cors = entityBrowserContext.addFilter(CrossOriginFilter.class,"/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");


        return entityBrowserContext;
    }

    private static ContextHandler buildContext1()
    {
        ServletContextHandler entityBrowserContext = new ServletContextHandler( ServletContextHandler.SESSIONS );
        entityBrowserContext.setContextPath( "/hello" );
        ResourceConfig security = new ResourceConfig();
        security.packages(HelloResource.class.getPackage().getName(), ExampleRequestFilterProvider.class.getPackage().getName());
        ServletContainer surityContainer = new ServletContainer( security );
        ServletHolder scentityBrowser = new ServletHolder( surityContainer );
        entityBrowserContext.addServlet( scentityBrowser, "/*" );
        return entityBrowserContext;
    }

    // This starts the Swagger UI at http://localhost:9999/docs
    private static ContextHandler buildSwaggerUI() throws Exception
    {
        final ResourceHandler swaggerUIResourceHandler = new ResourceHandler();
        swaggerUIResourceHandler.setResourceBase( App.class.getClassLoader().getResource( "swaggerui" ).toURI().toString() );

        final ContextHandler swaggerUIContext = new ContextHandler();
        swaggerUIContext.setContextPath( "/docs/" );
        swaggerUIContext.setHandler( swaggerUIResourceHandler );

        return swaggerUIContext;
    }
}

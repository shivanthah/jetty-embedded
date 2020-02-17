package com.shivanthah.jetty.rest.auth;

import com.shivanthah.jetty.rest.AuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class ExampleRequestFilterProvider implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        AuthenticationFilter exampleRequestLoggingFilter = new AuthenticationFilter(resourceInfo);
       featureContext.register(exampleRequestLoggingFilter);
    }

}

package com.example.helloworld;

/**
 * Created by frances on 6/25/16.
 */


import com.example.helloworld.JWT.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.example.helloworld.resources.HelloWorldResource;
import com.example.helloworld.health.TemplateHealthCheck;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.consumer.JwtConsumer;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    public static RsaJsonWebKey key;

    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) {
        key = JwtClaim.generateKey();

        environment.jersey().register(new AuthDynamicFeature(getAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new UserFactory());

        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.jersey().register(resource);

        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

    public AuthFilter getAuthFilter()
    {
        JwtConsumer consumer = JwtClaim.getJwtConsumer(key);

        JwtAuthFilter auth = new JwtAuthFilter.Builder<User>()
                .setJwtConsumer(consumer)
                .setRealm("realm")
                .setPrefix("Bearer")
                .setAuthenticator(new JwtAuthenticator())
                .setAuthorizer(new JwtAuthorizer())
                .buildAuthFilter();

        return auth;
    }

}

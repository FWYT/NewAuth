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
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
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

        //shiro
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("shiro.ini");
        SecurityManager sM = factory.getInstance();
        SecurityUtils.setSecurityManager(sM);

        //Subject user = SecurityUtils.getSubject();
        //System.out.println("\n\n\nUser is authenticated: " + user.isAuthenticated());



        /*UsernamePasswordToken token = new UsernamePasswordToken("cn=Frances Tso,cn=user,ou=users,dc=test,dc=com","Ft123456");
        user.login(token);
        System.out.println("User is authenticated: " + user.getPrincipal() + " " + user.isAuthenticated() + "\n");

        UsernamePasswordToken token2 = new UsernamePasswordToken("cn=Me Me,cn=admin,ou=users,dc=test,dc=com","ft123456");
        Subject user2 = SecurityUtils.getSubject();
        user2.login(token2);
        System.out.println("User2 is authenticated: " + user2.getPrincipal() + " " + user2.isAuthenticated() + "\n");*/

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

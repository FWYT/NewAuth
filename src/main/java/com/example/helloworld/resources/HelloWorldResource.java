package com.example.helloworld.resources;

/**
 * Created by frances on 6/25/16.
 */

import com.example.helloworld.api.Saying;
import org.jose4j.jwk.RsaJsonWebKey;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.helloworld.JWT.JwtClaim.createJwt;
import static com.example.helloworld.JWT.JwtClaim.validateJwt;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    public static RsaJsonWebKey key;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        this.key = com.example.helloworld.HelloWorldApplication.key;
    }

    @GET
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register()
    {
        String jwt = createJwt(key);
        return Response.ok(jwt, MediaType.APPLICATION_JSON).build();
    }



    @GET
    @PermitAll
    @Path("/hello")
    public Response sayHello(@QueryParam("token") String token) {
        if (token == null)
        {
            return Response.status(403).build();
        }

        if (validateJwt(key, token))
        {
            final String value = String.format(template, defaultName);
            return Response.ok(new Saying(counter.incrementAndGet(), value), MediaType.APPLICATION_JSON).build();
        }
        else
        {
            return Response.status(403).build();
        }
    }

}

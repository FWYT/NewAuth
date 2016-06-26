package com.example.helloworld.JWT;

/**
 * Created by frances on 6/26/16.
 */

import com.example.helloworld.JWT.User;
import com.example.helloworld.JWT.JwtAuthFilter;

import io.dropwizard.auth.Authenticator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;

import java.security.Principal;
import com.google.common.base.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticator implements Authenticator<JwtContext, User>{

    /**
     * Private internal log instance.
     */
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticator.class);

    //@Override
    public Optional<User> authenticate(JwtContext context) {
        // Provide your own implementation to lookup users based on the principal attribute in the
        // JWT Token. E.g.: lookup users from a database etc.
        // This method will be called once the token's signature has been verified

        // In case you want to verify different parts of the token you can do that here.
        // E.g.: Verifying that the provided token has not expired.

        // All JsonWebTokenExceptions will result in a 401 Unauthorized response.

        try {
            final Subject securitySubject = SecurityUtils.getSubject();
            final String subject = context.getJwtClaims().getSubject();
            final JwtAuthenticationToken token = new JwtAuthenticationToken(subject, context.getJwt());
            securitySubject.login(token);
            if (log.isDebugEnabled()) {
                final String message = "Authenticating with subject [" + subject + "]";
                log.debug(message);
            }
            return Optional.of(new User(securitySubject));
        }
        catch (MalformedClaimException ex) {
            final String message = "Malformed claim";
            log.error(message, ex);
            return Optional.absent();
        }
    }
}

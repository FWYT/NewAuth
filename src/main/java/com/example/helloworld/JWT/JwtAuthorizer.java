package com.example.helloworld.JWT;

/**
 * Created by frances on 6/26/16.
 */

import com.example.helloworld.JWT.User;
import io.dropwizard.auth.Authorizer;
import org.apache.shiro.authz.AuthorizationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthorizer  implements Authorizer<User>{
    /**
     * Private internal log instance.
     */
    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizer.class);

    //@Override
    public boolean authorize(User user, String role) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Checking authorization for user {} with role {}", user, role);
            }
            user.checkRole(role);
            return true;
        } catch (AuthorizationException ex) {
            final String message = "Authorization error for user [" + user.getName() + "]";
            log.error(message, ex);
            return false;
        }
    }
}

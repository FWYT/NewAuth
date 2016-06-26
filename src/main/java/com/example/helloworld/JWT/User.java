package com.example.helloworld.JWT;

/**
 * Created by frances on 6/26/16.
 */
import java.security.Principal;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User implements Principal {

    /**
     * Private internal log instance.
     */
    private static final Logger log = LoggerFactory.getLogger(User.class);

    private final Subject subject;

    public User(Subject subject) {
        super();
        if (subject == null)
            throw new NullPointerException();
        this.subject = subject;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public <T> T unwrap(Class<T> type) {
        if (Subject.class.equals(type)) return type.cast(subject);

        throw new IllegalArgumentException("User " + this + " cannot be unwrapped to " + type);
    }

    @Override
    public String toString() {
        String username = subject.getPrincipal() != null ? subject.getPrincipal().toString() : null;
        return username != null ? username : "anonymous";
    }

    public void checkPermissionBySomeRule() throws AuthorizationException {
        // Apply domain specific authorization rules based on data found in the user's data, subject principals etc.
        if (Math.random() < 0.5) throw new UnauthorizedException();
    }

    // Convenience delegate methods to the Subject

    public void checkPermission(String permission) throws AuthorizationException {
        subject.checkPermission(permission);
    }

    public void checkRole(String roleIdentifier) throws AuthorizationException {
        this.subject.checkRole(roleIdentifier);
    }

    public String getName() {
        return this.toString();
    }
}

package com.example.helloworld.JWT;

/**
 * Created by frances on 6/26/16.
 */

import org.apache.shiro.SecurityUtils;
import org.secnod.shiro.jersey.TypeFactory;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserFactory extends TypeFactory<User> {

    /**
     * Private internal log instance.
     */
    private static final Logger log = LoggerFactory.getLogger(UserFactory.class);

    public UserFactory() {
        super(User.class);
    }

    //@Override
    public User provide() {
        return new User(SecurityUtils.getSubject());
    }

}

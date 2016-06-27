package com.example.helloworld.JWT;

import org.apache.shiro.authc.AuthenticationToken;
/**
 * Created by frances on 6/26/16.
 */
import org.apache.shiro.authc.UsernamePasswordToken;

public class JwtAuthenticationToken extends UsernamePasswordToken {

    public JwtAuthenticationToken(String username, String password) {
        super(username, password);
    }
}

package com.example.helloworld.JWT;

import org.apache.shiro.authc.AuthenticationToken;
/**
 * Created by frances on 6/26/16.
 */
public class JwtAuthenticationToken implements AuthenticationToken{

    private Object userId;
    private String token;

    public JwtAuthenticationToken(Object userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    //@Override
    public Object getPrincipal() {
        return getUserId();
    }

    //@Override
    public Object getCredentials() {
        return getToken();
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

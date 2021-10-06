package com.example.demo.security.user;

import java.util.Map;

public class FacebookAuthUserInfo extends AuthUserInfo{
    public FacebookAuthUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }
}

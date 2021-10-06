package com.example.demo.security.user;

import java.util.Map;

public class GoogleAuthUserInfo extends AuthUserInfo {

    public GoogleAuthUserInfo(Map<String, Object> attributes) {
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

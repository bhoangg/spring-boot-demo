package com.example.demo.security.user;

import java.util.Map;

public abstract class AuthUserInfo {
    protected Map<String, Object> attributes;

    public AuthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();
}

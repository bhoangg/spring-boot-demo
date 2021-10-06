package com.example.demo.security.user;

import com.example.demo.exception.AuthProcessException;
import com.example.demo.model.AuthProvider;

import java.util.Map;

public class UserInfoFactory {

    public static AuthUserInfo getAuthUserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleAuthUserInfo(attributes);
        } else if(registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookAuthUserInfo(attributes);
        } else {
            throw new AuthProcessException("Sorry! Login with " + registrationId + " is not supported yet");
        }
    }
}

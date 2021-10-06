package com.example.demo.service;

import com.example.demo.exception.AuthProcessException;
import com.example.demo.model.AuthProvider;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import com.example.demo.security.UserPrincipal;
import com.example.demo.security.user.AuthUserInfo;
import com.example.demo.security.user.UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

@Service
public class UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest authUserRequest) throws OAuth2AuthenticationException {
        OAuth2User authUser = super.loadUser(authUserRequest);

        try {
            return processOAuth2User(authUserRequest, authUser);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            // Throw an istance of AuthenticationException will trigger the AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        AuthUserInfo authUserInfo = UserInfoFactory.getAuthUserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(authUserInfo.getEmail())) {
            throw new AuthProcessException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(authUserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new AuthProcessException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, authUserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, authUserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest authUserRequest, AuthUserInfo authUserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(authUserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(authUserInfo.getId());
        user.setUsername(authUserInfo.getName());
        user.setUseremail(authUserInfo.getEmail());

        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, AuthUserInfo authUserInfo) {
        existingUser.setUsername(authUserInfo.getName());
        return userRepository.save(existingUser);
    }
}





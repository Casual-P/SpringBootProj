package com.example.springjwtauth.security.oauth2.impl;

import com.example.springjwtauth.exeption.CustomAccessDeniedException;
import com.example.springjwtauth.exeption.CustomUserWasBannedException;
import com.example.springjwtauth.security.oauth2.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final RestTemplate restTemplate;


    private final Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();

    private final OauthService oauthService;

    private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";

    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";

    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE =
            new ParameterizedTypeReference<>(){};

    @PostConstruct
    public void postConstruct() {
        this.restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        CustomOauth2UserDetails oAuth2User;

        if (!userRequest.getClientRegistration().getRegistrationId().equals("vk")) {
            oAuth2User = loadDefaultUser(userRequest);
        }
        else {
            oAuth2User = loadUserFromVk(userRequest);
        }

        oAuth2User.setProvider(userRequest.getClientRegistration().getRegistrationId());

        try {
            oauthService.find(oAuth2User);
            oauthService.update(oAuth2User);
        }
        catch (NoSuchElementException ex) {
            oauthService.save(oAuth2User);
        }

        if(oAuth2User.isBanned())
            throw new CustomAccessDeniedException("User was disabled or banned");

        return oAuth2User;
    }

    @SuppressWarnings("all")
    private CustomOauth2UserDetails loadDefaultUser(OAuth2UserRequest userRequest) {
        String userNameAttributeName = getUserAttributeName(userRequest);
        Map<String, Object> userAttributes = process(userRequest);
        Set<GrantedAuthority> authorities = createAuthorities(userAttributes, userRequest);
        CustomOauth2UserDetails oauth2UserDetails = new CustomOauth2UserDetails(authorities, userAttributes, userNameAttributeName);
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oauth2UserDetails.setUserOauthId(oauth2UserDetails.getAttribute("sub").toString());
        }
        else {
            oauth2UserDetails.setUserOauthId(oauth2UserDetails.getAttribute("id").toString());
        }
        oauth2UserDetails.setEmail(oauth2UserDetails.getAttribute("email"));
        return oauth2UserDetails;
    }

    private Set<GrantedAuthority> createAuthorities(Map<String, Object> userAttributes, OAuth2UserRequest userRequest) {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OAuth2UserAuthority(userAttributes));
        OAuth2AccessToken token = userRequest.getAccessToken();
        for (String authority : token.getScopes()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
        }
        return authorities;
    }



    @SuppressWarnings("all")
    private CustomOauth2UserDetails loadUserFromVk(OAuth2UserRequest userRequest) {
        String userNameAttributeName = getUserAttributeName(userRequest);
        Map<String, Object> wrapperAttributes = process(userRequest);
        if (wrapperAttributes == null) {
            throw new IllegalStateException("Attributes must be not null");
        }
        List<Map<String, Object>> wrapperAttributesList = (List<Map<String, Object>>) wrapperAttributes.get("response");
        Map<String, Object> userAttributes = wrapperAttributesList.get(0);
        Set<GrantedAuthority> authorities = createAuthorities(userAttributes, userRequest);
        CustomOauth2UserDetails oauth2UserDetails = new CustomOauth2UserDetails(authorities, userAttributes, userNameAttributeName);
        oauth2UserDetails.setUserOauthId(oauth2UserDetails.getAttribute("id").toString());
        oauth2UserDetails.setEmail(userRequest.getAdditionalParameters().get("email").toString());
        return oauth2UserDetails;
    }

    private String getUserAttributeName(OAuth2UserRequest userRequest) {
        Assert.notNull(userRequest, "User Request cannot be null");
        if (!StringUtils
                .hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE,
                    "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: "
                            + userRequest.getClientRegistration().getRegistrationId(),
                    null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        if (!StringUtils.hasText(userNameAttributeName)) {
            OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,
                    "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: "
                            + userRequest.getClientRegistration().getRegistrationId(),
                    null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        return userNameAttributeName;
    }

    private Map<String, Object> process(OAuth2UserRequest userRequest) {
        RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);
        ResponseEntity<Map<String, Object>> response;
        try {
            Assert.notNull(request, "Request must be not null");
            response = this.restTemplate.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        }
        catch (OAuth2AuthorizationException ex) {
            OAuth2Error oauth2Error = ex.getError();
            StringBuilder errorDetails = new StringBuilder();
            errorDetails.append("Error details: [");
            errorDetails.append("UserInfo Uri: ")
                    .append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
            errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
            if (oauth2Error.getDescription() != null) {
                errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
            }
            errorDetails.append("]");
            oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                    "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails,
                    null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        }
        catch (UnknownContentTypeException ex) {
            String errorMessage = "An error occurred while attempting to retrieve the UserInfo Resource from '"
                    + userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()
                    + "': response contains invalid content type '" + ex.getContentType() + "'. "
                    + "The UserInfo Response should return a JSON object (content type 'application/json') "
                    + "that contains a collection of name and value pairs of the claims about the authenticated End-User. "
                    + "Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration '"
                    + userRequest.getClientRegistration().getRegistrationId() + "' conforms to the UserInfo Endpoint, "
                    + "as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'";
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, errorMessage, null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        }
        catch (RestClientException ex) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                    "An error occurred while attempting to retrieve the UserInfo Resource: " + ex.getMessage(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        }
        return response.getBody();
    }
}

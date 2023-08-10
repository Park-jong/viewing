package com.ssafy.interviewstudy.support.member;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AccessTokenSupport {
    private SocialLoginType socialLoginType;
    private String authorizeCode;
    public AccessTokenSupport(SocialLoginType socialLoginType, String authorizeCode) {
        this.socialLoginType = socialLoginType;
        this.authorizeCode = authorizeCode;
    }
    public AccessTokenResult getAccessToken(OauthUriSupport oauthUriSupport){

        HttpHeaders httpHeaders = new HttpHeaders();

        //요청 헤더 content type설정
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");


        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("grant_type", "authorization_code");
        bodyMap.add("client_id", oauthUriSupport.getClientId(socialLoginType));
        bodyMap.add("redirect_uri", oauthUriSupport.getRedirectUri(socialLoginType));
        bodyMap.add("code", authorizeCode);
        bodyMap.add("client_secret", oauthUriSupport.getClientSecret(socialLoginType));

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(bodyMap, httpHeaders);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<Map> getTokenResponse;

        try {
            getTokenResponse = rt.exchange(
                    oauthUriSupport.getSocialLoginTokenUri(socialLoginType),
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            return new AccessTokenResult(null,false);
        }

        String accessToken = (String) getTokenResponse.
                getBody().
                get("access_token");

        return new AccessTokenResult(accessToken,true);
    }
}

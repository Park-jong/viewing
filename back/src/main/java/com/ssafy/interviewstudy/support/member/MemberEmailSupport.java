package com.ssafy.interviewstudy.support.member;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemberEmailSupport implements MemberInfoSupport{
    @Override
    public MemberInfoResult getMemberInfo(SocialLoginType socialLoginType,String accessToken) {

        HttpHeaders getMemberInfoHttpHeaders = new HttpHeaders();

        //요청 헤더 content type설정 accesstoken 설정
        getMemberInfoHttpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        getMemberInfoHttpHeaders.add("Authorization", "Bearer " + accessToken);
        //uri 설정
        UriComponentsBuilder getMemberInfoUriComponentsBuilder =
                UriComponentsBuilder.fromUriString(OauthUriSupport.getMemberInfoUri(socialLoginType));

        //유저정보 요청 응답
        ResponseEntity<Map> getMemberInfoResponse;

        //유저정보 요청 바디
        MultiValueMap<String, Object> getMemberInfoBodyMap = new LinkedMultiValueMap<>();
        getMemberInfoBodyMap.add("property_keys", "[\"kakao_account.email\"]");

        //유저정보 바디+헤더
        HttpEntity<MultiValueMap<String, Object>> getMemberInfoHttpEntity
                = new HttpEntity<>(getMemberInfoBodyMap, getMemberInfoHttpHeaders);

        //요청 객체
        RestTemplate rt = new RestTemplate();
        URI uri = getMemberInfoUriComponentsBuilder.build().toUri();
        HttpMethod requestHttpMethod = HttpMethod.POST;
        try{
            if(socialLoginType==SocialLoginType.github) requestHttpMethod = HttpMethod.GET;
            //요청을 통해 유저 정보를 받아옴
            getMemberInfoResponse = rt.exchange(
                    getMemberInfoUriComponentsBuilder.build().toUri(),
                    requestHttpMethod,
                    getMemberInfoHttpEntity,
                    Map.class
            );
        }
        catch(HttpClientErrorException ex){
            ex.printStackTrace();
            return new MemberInfoResult(null,false);
        }
        LinkedHashMap responseMap;
        String email = null;
        try{
            responseMap = (LinkedHashMap) getMemberInfoResponse
                    .getBody();

            if(socialLoginType==SocialLoginType.kakao){
                responseMap = (LinkedHashMap) responseMap.get("kakao_account");
            }
            email = (String)responseMap.get("email");
        }
        catch(Exception e){
            return new MemberInfoResult(null,false);
        }
        return new MemberInfoResult(email,email!=null);
    }
}

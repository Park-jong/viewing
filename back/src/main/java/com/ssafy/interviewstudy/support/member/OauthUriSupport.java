package com.ssafy.interviewstudy.support.member;

public class OauthUriSupport {
    //소셜 로그인 화면 URI
    public static String getSocialLoginUri(SocialLoginType socialLoginType){
        if(socialLoginType==SocialLoginType.kakao){
            return "https://kauth.kakao.com/oauth/authorize";
        }
        if(socialLoginType==SocialLoginType.google){
            return "https://accounts.google.com/o/oauth2/v2/auth";
        }
        if(socialLoginType==SocialLoginType.github){
            return "https://github.com/login/oauth/authorize";
        }
        //기본 카카오
        return "https://kauth.kakao.com/oauth/authorize";
    }

    //액세스 토큰 획득 페이지 URI
    public static String getSocialLoginTokenUri(SocialLoginType socialLoginType){
        if(socialLoginType==SocialLoginType.kakao){
            return "https://kauth.kakao.com/oauth/token";
        }
        if(socialLoginType==SocialLoginType.google){
            return "https://oauth2.googleapis.com/token";
        }
        if(socialLoginType==SocialLoginType.github){
            return "https://github.com/login/oauth/access_token";
        }
        //기본 카카오
        return "https://kauth.kakao.com/oauth/token";
    }

    //Oauth 2.0 클라이언트ID
    public static String getClientId(SocialLoginType socialLoginType){
        if(socialLoginType==SocialLoginType.kakao){
            return "2869d34ce0755e095b7d39e5eb3aeafb";
        }
        if(socialLoginType==SocialLoginType.google){
            return "249028033375-3q56vn82p2jku86es16u191kflqp6p1o.apps.googleusercontent.com";
        }
        if(socialLoginType==SocialLoginType.github){
            return "849f92ca3e7a5a3a76da";
        }
        //기본 카카오
        return "2869d34ce0755e095b7d39e5eb3aeafb";
    }

    public static String getRedirectUri(SocialLoginType socialLoginType){
        return "http://localhost:3000/login/loading";
    }

    public static String getClientSecret(SocialLoginType socialLoginType){
        if(socialLoginType==SocialLoginType.kakao){
            return "mGDziiY3mWQqXCxnCR2quFwTBqBO4hTh";
        }
        if(socialLoginType==SocialLoginType.google){
            return "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";
        }
        if(socialLoginType==SocialLoginType.github){
            return "0afdf2454c32470bfc1360d72daadbf74075b1c8";
        }
        //기본 카카오
        return "mGDziiY3mWQqXCxnCR2quFwTBqBO4hTh";
    }

    public static String getMemberInfoUri(SocialLoginType socialLoginType){
        if(socialLoginType==SocialLoginType.kakao){
            return "https://kapi.kakao.com/v2/user/me";
        }
        if(socialLoginType==SocialLoginType.google){
            return "https://www.googleapis.com/oauth2/v3/userinfo";
        }
        if(socialLoginType==SocialLoginType.github){
            return "https://api.github.com/user";
        }
        //기본 카카오
        return "https://kapi.kakao.com/v2/user/me";
    }
}

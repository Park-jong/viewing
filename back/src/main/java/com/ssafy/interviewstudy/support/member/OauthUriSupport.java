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
            return "a684cbf16b718d9b4f5ab2006f679570";
        }
        if(socialLoginType==SocialLoginType.google){
            return "305569629426-vuadrf7g4tu5akoin7mu5faahukm2jhe.apps.googleusercontent.com";
        }
        if(socialLoginType==SocialLoginType.github){
            return "df9877e05a0df7be700b";
        }
        //기본 카카오
        return "a684cbf16b718d9b4f5ab2006f679570";
    }

    public static String getRedirectUri(SocialLoginType socialLoginType){
        return "http://localhost:8080/oauth/"+socialLoginType.toString();
    }

    public static String getClientSecret(SocialLoginType socialLoginType){
        if(socialLoginType==SocialLoginType.kakao){
            return "txNZUThxM2ot63rNm0eJwqGVifYqpA8m";
        }
        if(socialLoginType==SocialLoginType.google){
            return "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";
        }
        if(socialLoginType==SocialLoginType.github){
            return "cb66d9ea480ffd1ea7a218c2eb6f7cb5ae335785";
        }
        //기본 카카오
        return "txNZUThxM2ot63rNm0eJwqGVifYqpA8m";
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

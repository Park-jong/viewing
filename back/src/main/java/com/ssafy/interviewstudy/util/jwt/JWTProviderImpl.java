package com.ssafy.interviewstudy.util.jwt;

import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.NoArgsConstructor;
import org.springframework.util.Base64Utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class JWTProviderImpl implements JWTProvider{
    private final String secretKey = "SongHeungMinBongJunhoJayParkLetsgooooooooooooo" +
            "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo";

    private String decodeToken(String token){
        return new String(Base64Utils.decodeFromUrlSafeString(token));
    }

    //토큰 유효성 검사
    @Override
    public JWTResponseType isValidToken(String token){
        try{
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();
            long expiration = claims.getExpiration().getTime();
            long now = System.currentTimeMillis();

            if(now>expiration){
                return JWTResponseType.TOKEN_EXPIRED;
            }
        }
        catch (ExpiredJwtException ex){
            return JWTResponseType.TOKEN_EXPIRED;
        }
        catch(WeakKeyException wk){
            return JWTResponseType.VALID_TOKEN;
        }
        catch(Exception ex){
            ex.printStackTrace();
            return JWTResponseType.INVALID_TOKEN;
        }
        return JWTResponseType.VALID_TOKEN;
    }

    //토큰 생성
    @Override
    public String provideToken(JWTMemberInfo jwtMemberInfo){
        Map<String, Object> claimMap = new HashMap<>();

        claimMap.put("sub","InterviewStudy");
        claimMap.put("exp", Instant.now().plusSeconds(60*60*24).getEpochSecond());
        claimMap.put("iat",Instant.now().getEpochSecond());

        claimMap.put("memberId", jwtMemberInfo.getMemberId());
        claimMap.put("email", jwtMemberInfo.getEmail());

        String retToken = Jwts.builder()
                .addClaims(claimMap)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        return retToken;
    }

    //토큰을 컨트롤러에서 필요한 DTO로 바꾸자!
    @Override
    public JWTMemberInfo getJWTMemberInfo(String token){
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token);

        Claims claims = jws.getBody();

        return JWTMemberInfo
                .builder()
                .email((String)claims.get("email"))
                .memberId((Integer)claims.get("memberId"))
                .build();
    }
}
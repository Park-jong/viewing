package com.ssafy.interviewstudy.controller.member;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.domain.member.*;
import com.ssafy.interviewstudy.dto.member.dto.MemberProfileChangeDto;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.member.jwt.JWTToken;
import com.ssafy.interviewstudy.service.member.MemberService;
import com.ssafy.interviewstudy.support.member.*;
import com.ssafy.interviewstudy.util.jwt.JWTProvider;
import com.ssafy.interviewstudy.util.jwt.JWTProviderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login/{socialLoginType}")
    public ResponseEntity<?> login(@PathVariable SocialLoginType socialLoginType){
        HttpHeaders httpHeaders = new HttpHeaders();
        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.
                        fromUriString(OauthUriSupport.getSocialLoginUri(socialLoginType)).
                        queryParam("client_id",OauthUriSupport.getClientId(socialLoginType)).
                        queryParam("redirect_uri",OauthUriSupport.getRedirectUri(socialLoginType)).
                        queryParam("response_type","code").
                        queryParam("prompt","login");

        if(socialLoginType==SocialLoginType.google) {
            uriComponentsBuilder.queryParam("scope", "email");
        }
        if(socialLoginType==SocialLoginType.github){
            uriComponentsBuilder.queryParam("scope","user:email");
        }
        URI responseRedirectUri = uriComponentsBuilder.build().toUri();
        httpHeaders.setLocation(responseRedirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/oauth/{socialLoginType}")
    public ResponseEntity<?> loginDone(@RequestParam(value = "code", required = false) String authorizeCode,
                                       @PathVariable SocialLoginType socialLoginType
    ) {
        if (authorizeCode == null) {
            //로그인 실패인데 이페이지 온거니까 리다이렉트 시켜버려
            HttpHeaders responseHttpHeaders = new HttpHeaders();
            //프론트 소셜 로그인페이지로 ㄱㄱ (어디 사이트로 할지 고르는 페이지)
            responseHttpHeaders.setLocation(URI.create(RedirectUriSupport.home));
            return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);
        }

        //access Token 가져오기
        AccessTokenSupport accessTokenSupport =
                new AccessTokenSupport(socialLoginType,authorizeCode);
        AccessTokenResult accessTokenResult = accessTokenSupport.getAccessToken();

        if(!accessTokenResult.getIsSuccess()){
            //로그인 실패
            HttpHeaders responseHttpHeaders = new HttpHeaders();
            responseHttpHeaders.setLocation(URI.create(RedirectUriSupport.home));
            return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);
        }

        String accessToken = accessTokenResult.getAccessToken();

        //accessToken으로 이메일 가져오기
        MemberInfoSupport memberInfoSupport = new MemberEmailSupport();
        MemberInfoResult memberInfoResult = memberInfoSupport.getMemberInfo(socialLoginType,accessToken);

        if(!memberInfoResult.getIsSucess()){
            //이메일 가져오는거 실패 (서버 잘못,,)
            HttpHeaders responseHttpHeaders = new HttpHeaders();
            responseHttpHeaders.setLocation(URI.create(RedirectUriSupport.home));
            return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);
        }

        //이메일롷 현재 멤버 찾아보기
        Member currentMember = memberService.findByEmail(memberInfoResult.getEmail());

        //이미 가입한 유저일때 이 부분을 인터셉터로 확인해야함
        //그냥 JWT토큰 쥐어주고 리다이렉트 시키자
        if(currentMember!=null){
            HttpHeaders responseHttpHeaders = new HttpHeaders();
            JWTProvider jwtProvider = new JWTProviderImpl();
            //jwt 토큰 세팅
            JWTToken jwtToken = JWTToken.builder().accessToken(
                    jwtProvider.provideToken(JWTMemberInfo.
                            builder().
                            memberId(currentMember.getId()).
                            email(currentMember.getEmail()).
                            build()
                    )
            ).build();
            return ResponseEntity.ok().body(jwtToken);
        }
        //랜덤 프로필 배경 만들기
        Long randomProfileBackground = System.currentTimeMillis()%(long)(MemberProfileBackground.values().length);
        Long randomProfileImage = System.currentTimeMillis()%(long)(MemberProfileImage.values().length);


        //가입한 유저가 아닐때
        Member registeredMember = Member.builder()
                .created_at(LocalDateTime.now())
                .accessToken(accessToken)
                .status(MemberStatus.ACTIVE)
                .registrationStatus(RegistrationStatus.SELECT_NICKNAME)
                .email(memberInfoResult.getEmail())
                .nickname("user"+System.currentTimeMillis())
                .memberProfileBackground(
                        MemberProfileBackground.values()[randomProfileBackground.intValue()]
                )
                .memberProfileImage(
                        MemberProfileImage.values()[randomProfileImage.intValue()]
                )
                .build();


        //우리쪽에 등록시키기
        memberService.register(registeredMember);

        //닉네임 고르러 가즈아
        //내가 리다이렉트 시키는게 맞는지 생각

        HttpHeaders responseHttpHeaders = new HttpHeaders();
        JWTProvider jwtProvider = new JWTProviderImpl();
        //jwt 토큰 세팅
        JWTToken jwtToken = JWTToken.builder().accessToken(
                jwtProvider.provideToken(JWTMemberInfo.
                        builder().
                        memberId(registeredMember.getId()).
                        email(registeredMember.getEmail()).
                        build()
                )
        ).build();
        return ResponseEntity.ok().body(jwtToken);
    }


    @GetMapping("/login/nickname/check/{nickname}")
    public ResponseEntity<?> checkDuplicateNickname(
                                                    @PathVariable(required = true) String nickname){

        Member checkedMember = memberService.checkDuplicateNickname(nickname);
        if(checkedMember==null){
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.badRequest().build();
    }


    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @PutMapping("/users/{userId}/nickname")
    public ResponseEntity<?> selectNickname(@RequestBody(required = true) String nickname,
                                            @PathVariable("userId") Integer memberId){

        //닉네임 중복체크
        Member checkedMember = memberService.checkDuplicateNickname(nickname);
        if(checkedMember==null){
            Member nowMember = memberService.findMemberByMemberId(
                    memberId
            );
            memberService.nextRegistrationStatus(nowMember);
            memberService.changeMemberNickname(nowMember,nickname);
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.badRequest().body("중복된 닉네임");
        }
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @PutMapping("/users/{userId}/profile")
    public ResponseEntity changeCharacter(@RequestBody MemberProfileChangeDto memberProfileChangeDto,
                                         @PathVariable Integer userId){
        memberProfileChangeDto.setUserId(userId);
        if(memberProfileChangeDto.getCharacter()==null &&
                memberProfileChangeDto.getBackground()==null) return ResponseEntity.badRequest().body("바꿀 필드가 모두 null입니다");
        memberService.changeMemberProfile(memberProfileChangeDto);
        return ResponseEntity.ok().build();
    }
}

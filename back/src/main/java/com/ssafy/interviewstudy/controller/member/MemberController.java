package com.ssafy.interviewstudy.controller.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.MemberStatus;
import com.ssafy.interviewstudy.domain.member.RegistrationStatus;
import com.ssafy.interviewstudy.dto.member.JWTMemberInfo;
import com.ssafy.interviewstudy.service.member.MemberService;
import com.ssafy.interviewstudy.support.member.*;
import com.ssafy.interviewstudy.util.JWTProvider;
import com.ssafy.interviewstudy.util.JWTProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home(){
        return "hi";
    }

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
            responseHttpHeaders.setBearerAuth(
                    jwtProvider.provideToken(JWTMemberInfo.
                            builder().
                            memberId(currentMember.getId()).
                            email(currentMember.getEmail()).
                            build()
                    )
            );
            //이미 가입했으면 거기로 리다이렉트 하면댐
            if(currentMember.getRegistrationStatus()==RegistrationStatus.FINISHED){
                responseHttpHeaders.setLocation(URI.create(RedirectUriSupport.home));
                return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);
            }
            //가입된 유저가 닉네임을 입력안했을때
            else if(currentMember.getRegistrationStatus()==RegistrationStatus.SELECT_NICKNAME){
                responseHttpHeaders.setLocation(URI.create(RedirectUriSupport.selectNickname));
                return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);
            }
            //가입된 유저가 프로필 안골랐을때
            else{
                responseHttpHeaders.setLocation(URI.create(RedirectUriSupport.selectProfile));
                return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);
            }
        }
        //가입한 유저가 아닐때
        Member registeredMember = Member.builder()
                .created_at(LocalDateTime.now())
                .accessToken(accessToken)
                .status(MemberStatus.ACTIVE)
                .registrationStatus(RegistrationStatus.SELECT_NICKNAME)
                .email(memberInfoResult.getEmail())
                .build();

        //우리쪽에 등록시키기
        memberService.register(registeredMember);

        //닉네임 고르러 가즈아
        //내가 리다이렉트 시키는게 맞는지 생각
        HttpHeaders responseHttpHeaders = new HttpHeaders();
        responseHttpHeaders.setLocation(URI.create(RedirectUriSupport.selectNickname));
        return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/login/nickname/check/{nickname}")
    public ResponseEntity<?> checkDuplicateNickname(@PathVariable(required = true) String nickname){
        Member checkedMember = memberService.checkDuplicateNickname(nickname);
        if(checkedMember==null){
            return ResponseEntity.ok().build();
        }
        else return ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/login/nickname")
    public ResponseEntity<?> selectNickname(@RequestBody(required = true) String nickname){

        //닉네임 중복체크
        Member checkedMember = memberService.checkDuplicateNickname(nickname);
        if(checkedMember!=null){
            HttpHeaders responseHttpHeaders = new HttpHeaders();
            /*
                JWT토큰으로 처리하는 부분 현재 페이지에 들어와있는 멤버가 누군지 알아야함
             */

            Member nowMember = memberService.findMemberByMemberId(1);
            memberService.nextRegistrationStatus(nowMember);
            memberService.changeMemberNickname(nowMember,nickname);

            responseHttpHeaders.setLocation(URI.create("http://localhost:8080/oauth"));
            return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);
        }
        else{
            memberService.nextRegistrationStatus(checkedMember);
            HttpHeaders responseHttpHeaders = new HttpHeaders();
            responseHttpHeaders.setLocation(URI.create("http://localhost:8080/oauth"));
            return new ResponseEntity<>(responseHttpHeaders, HttpStatus.MOVED_PERMANENTLY);

        }
    }
}

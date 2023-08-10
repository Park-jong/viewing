package com.ssafy.interviewstudy.controller.member;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.domain.member.*;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.member.MemberNicknameChangeDto;
import com.ssafy.interviewstudy.dto.member.MemberProfileChangeDto;
import com.ssafy.interviewstudy.dto.member.ProfileResponseDto;
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
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final OauthUriSupport oauthUriSupport;

    @PostMapping("/login/{socialLoginType}")
    public ResponseEntity<?> login(@PathVariable SocialLoginType socialLoginType){
        HttpHeaders httpHeaders = new HttpHeaders();
        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.
                        fromUriString(OauthUriSupport.getSocialLoginUri(socialLoginType)).
                        queryParam("client_id",oauthUriSupport.getClientId(socialLoginType)).
                        queryParam("redirect_uri",oauthUriSupport.getRedirectUri(socialLoginType)).
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
            return ResponseEntity.badRequest().body("auth code가 없습니다");
        }

        //access Token 가져오기
        AccessTokenSupport accessTokenSupport =
                new AccessTokenSupport(socialLoginType,authorizeCode);
        AccessTokenResult accessTokenResult = accessTokenSupport.getAccessToken(oauthUriSupport);

        if(!accessTokenResult.getIsSuccess()){
            //로그인 실패
            return ResponseEntity.badRequest().body("accessToken 가져오기 실패");
        }

        String accessToken = accessTokenResult.getAccessToken();

        //accessToken으로 이메일 가져오기
        MemberInfoSupport memberInfoSupport = new MemberIdPlatformSupport();
        MemberInfoResult memberInfoResult = memberInfoSupport.getMemberInfo(socialLoginType,accessToken);

        if(!memberInfoResult.getIsSucess()){
            //이메일 가져오는거 실패 (서버 잘못,,)
            return ResponseEntity.badRequest().body("이메일 가져오기 실패");
        }

        //이메일롷 현재 멤버 찾아보기
//        Member currentMember = memberService.findByEmail(memberInfoResult.getEmail());

        //플랫폼과 고유 Id로 멤버 찾아보기
        Member currentMember = memberService
                .findByIdAndPlatform(memberInfoResult.getMemberId(),memberInfoResult.getSocialLoginType());

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
                .socialLoginId(memberInfoResult.getMemberId())
                .socialLoginType(memberInfoResult.getSocialLoginType())
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
    @PutMapping("/members/{memberId}/nickname")
    public ResponseEntity<?> selectNickname(@RequestBody(required = true)MemberNicknameChangeDto memberNicknameChangeDto,
                                            @PathVariable("memberId") Integer memberId){

        //닉네임 중복체크
        Member checkedMember = memberService.checkDuplicateNickname(memberNicknameChangeDto.getNickname());
        if(checkedMember==null){
            Member nowMember = memberService.findMemberByMemberId(
                    memberId
            );
            memberService.nextRegistrationStatus(nowMember);
            memberService.changeMemberNickname(nowMember,memberNicknameChangeDto.getNickname());
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.badRequest().body("중복된 닉네임");
        }
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping("/members/{memberId}")
    public ResponseEntity retrieveProfile(@MemberInfo JWTMemberInfo memberInfo,
                                          @PathVariable Integer memberId){
        ProfileResponseDto profileResponseDto = new ProfileResponseDto(memberService.findMemberByMemberId(memberId));
        return ResponseEntity.ok(profileResponseDto);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @PutMapping("/members/{memberId}/profile")
    public ResponseEntity changeCharacter(@RequestBody MemberProfileChangeDto memberProfileChangeDto,
                                         @PathVariable Integer memberId){
        memberProfileChangeDto.setMemberId(memberId);
        if(memberProfileChangeDto.getCharacter()==null &&
                memberProfileChangeDto.getBackground()==null) return ResponseEntity.badRequest().body("바꿀 필드가 모두 null입니다");
        memberService.changeMemberProfile(memberProfileChangeDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/createMemberTest")
    public ResponseEntity changeCharacter(@RequestParam("code") String code){

        if(!code.equals("a205")){
            return ResponseEntity.badRequest().body("코드가 잘못됐습니다 당신 누구야");
        }
        Member testMember = Member.builder()
                .created_at(LocalDateTime.now())
                .status(MemberStatus.ACTIVE)
                .registrationStatus(RegistrationStatus.FINISHED)
                .email("test123@viewing.com")
                .nickname("user"+System.currentTimeMillis())
                .memberProfileBackground(
                        MemberProfileBackground.COLOR_93ffff
                )
                .memberProfileImage(
                        MemberProfileImage.cow
                )
                .build();

        memberService.register(testMember);

        return ResponseEntity.ok(testMember);
    }
}

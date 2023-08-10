package com.ssafy.interviewstudy.domain.member;

import com.ssafy.interviewstudy.domain.board.*;
import com.ssafy.interviewstudy.domain.calendar.Calendar;
import com.ssafy.interviewstudy.domain.message.Message;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.support.member.SocialLoginType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//회원 정보 엔티티(소셜로그인으로 들어오는 사용자 모두)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //회원가입에 필요한 이메일 (향후 소셜로그인 회원가입시 중복체크)
    @Column(name = "email", nullable = false)
    private String email;

    //유저의 닉네임 칼럼
    @Column(name = "nickname",length = 45)
    private String nickname;

    /* 유저 프로필 배경관련 필드 존재해야함
            배경
            캐릭터
       프론트와 협의 필요
     */


    //멤버 프로필 배경 색깔 기본값 설정 #9c209c
    @Column(name="profile_background")
    @Enumerated(EnumType.STRING)
    MemberProfileBackground memberProfileBackground = MemberProfileBackground.COLOR_9c209c;

    //멤버 프로필 캐릭터 동물 지정 기본값 해파리
    @Column(name="profile_image")
    @Enumerated(EnumType.STRING)
    MemberProfileImage memberProfileImage = MemberProfileImage.jellyfish;

    //소셜로그인 OAuth2.0 관련 토큰 필드
    @Column(name= "access_token")
    private String accessToken;

    //회원 생성일
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime created_at;

    //회원이 마지막으로 로그인시도를 한 시간
    @Column(name = "last_login")
    private LocalDateTime last_login;

    //탈퇴한 회원의 상태 -> 어떻게 탈퇴했는지에 대한 구분
    @Column(name = "member_status")
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    //언제 탈퇴했는지 시간기록
    @Column(name = "inactive_at")
    private LocalDateTime inactiveAt;

    //가입한 플랫폼 이름
    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;

    //소셜로그인에서 제공되는 PK
    private String socialLoginId;

    @Column(name = "registration_status")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    //게시판 댓글 작성자
    @OneToMany(mappedBy = "author")
    List<ArticleComment> articleCommentList = new ArrayList<>();

    //게시글 좋아요 누른사람
    @OneToMany(mappedBy = "member")
    List<ArticleLike> articleLikeList = new ArrayList<>();
    
    //게시글 작성자
    @OneToMany(mappedBy = "author")
    List<Board> boardList = new ArrayList<>();
    
    //게시글 댓글 좋아요 누른 사람
    @OneToMany(mappedBy = "member")
    List<CommentLike> commentLikeList = new ArrayList<>();

    //게시글 신고 누른사람
    @OneToMany(mappedBy = "member")
    List<ReportArticle> reportArticleList = new ArrayList<>();

    //스터디 리더
    @OneToMany(mappedBy = "leader")
    List<Study> studyList = new ArrayList<>();
    
    //스터디에 북마크 누른 살마
    @OneToMany(mappedBy = "member")
    List<StudyBookmark> studyBookmarkList = new ArrayList<>();

    //스터디 일정 등록한 사람
    @OneToMany(mappedBy = "author")
    List<StudyCalendar> studyCalendarList = new ArrayList<>();

    //스터디 채팅 입력한 사람
    @OneToMany(mappedBy = "author")
    List<StudyChat> studyChatList = new ArrayList<>();

    //스터디에 속해있는 사람
    @OneToMany(mappedBy = "member")
    List<StudyMember> studyMemberList = new ArrayList<>();

    //스터디에 가입 신청을 한 사람
    @OneToMany(mappedBy = "applicant")
    List<StudyRequest> studyRequestList = new ArrayList<>();

    //메세지 보낸 사람
    @OneToMany(mappedBy = "author")
    List<Message> sentMessageList = new ArrayList<>();

    //메세지 받은 사람
    @OneToMany(mappedBy="receiver")
    List<Message> receivedMessageList = new ArrayList<>();

    @OneToMany(mappedBy="author")
    List<Calendar> calendarList = new ArrayList<>();

    public void nextRegistrationStatus(){
        if(this.registrationStatus==RegistrationStatus.SELECT_NICKNAME){
            this.registrationStatus = RegistrationStatus.SELECT_PROFILE;
        }
        else if(this.registrationStatus==RegistrationStatus.SELECT_PROFILE){
            this.registrationStatus = RegistrationStatus.FINISHED;
        }
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeMemberProfileBackground(MemberProfileBackground memberProfileBackground){
        this.memberProfileBackground = memberProfileBackground;
    }

    public void changeMemberProfileImage(MemberProfileImage memberProfileImage){
        this.memberProfileImage = memberProfileImage;
    }
    @Builder
    public Member(Integer id, String email, String nickname, MemberProfileBackground memberProfileBackground, MemberProfileImage memberProfileImage, String accessToken, LocalDateTime created_at, LocalDateTime last_login, MemberStatus status, LocalDateTime inactiveAt, SocialLoginType socialLoginType, String socialLoginId, RegistrationStatus registrationStatus) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.memberProfileBackground = memberProfileBackground;
        this.memberProfileImage = memberProfileImage;
        this.accessToken = accessToken;
        this.created_at = created_at;
        this.last_login = last_login;
        this.status = status;
        this.inactiveAt = inactiveAt;
        this.socialLoginType = socialLoginType;
        this.socialLoginId = socialLoginId;
        this.registrationStatus = registrationStatus;
    }
}

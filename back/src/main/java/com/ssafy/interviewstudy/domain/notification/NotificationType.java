package com.ssafy.interviewstudy.domain.notification;

public enum NotificationType {


    //스터디에서 내가 리더가됨
    //어디 스터디
    Leader,
    //스터디 신청 승인/거부/ 스터디장이 신청받은거
    //어디 스터디인지
    StudyRequest,
    StudyRequest_Approve,
    StudyRequest_Reject,
    //스터디 게시판에 게시글이 등록됨 (스터디원 전원)
    //어디 스터디인지 제목
    StudyArticle,
    //스터디에 영상회의가 열림 (스터디원 전원)
    //영상회의가 열린 스터디
    StudyMeeting,
    //스터디 게시판에 내 게시글에 댓글이 달림
    //스터디이름 게시글 제목
    StudyComment,
    //스터디에 일정이 등록됨
    //스터디이름
    StudyCalendar,
    //스터디 게시판 내 댓글에 답글이 달림
    //스터디 이름 댓글 내용
    StudyReply,
    //게시판에 내 게시글에 댓글이 달림
    //어디 게시판, 게시글 제목
    BoardComment,
    //게시판에 내 댓글에 답글이 달림
    //어디 게시판, 댓글 내용
    BoardReply,
    //메시지 수신
    //들어갈 정보 (새 쪽지가 도착했습니다.)
    Message,
    //테스트용
    Test,
    //영상회의 생김
    Conference
}

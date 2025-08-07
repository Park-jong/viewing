package com.ssafy.interviewstudy.service.study.studyChat;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.study.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyChatService {
    //스터디 실시간 채팅 작성
    ChatResponse addChat(Integer studyId, ChatRequest chat);

    //스터디 실시간 채팅 조회
    List<ChatResponse> findStudyChats(Integer studyId, Integer lastChatId);

    //이전 채팅 보기
    List<ChatResponse> findOldStudyChats(Integer studyId, Integer startChatId);
}

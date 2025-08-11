package com.ssafy.interviewstudy.service.study.studyChat;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.exception.calendar.updateFailException;
import com.ssafy.interviewstudy.exception.study.StudyExceptionFactory;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyChatServiceImpl implements StudyChatService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyChatRepository studyChatRepository;

    //스터디 실시간 채팅 작성
    @Transactional
    @Override
    public ChatResponse addChat(Integer studyId, ChatRequest chat){
        Member member = memberRepository.findById(chat.getMemberId()).orElseThrow(StudyExceptionFactory::studyMemberNotFound);
        Study study = studyRepository.findById(studyId).orElseThrow(StudyExceptionFactory::studyNotFound);
        if(!checkStudyMember(study.getId(), member.getId())) throw new updateFailException("잘못된 접근");
        StudyChat studyChat = new StudyChat(study, member, chat.getContent());
        studyChatRepository.save(studyChat);
        return new ChatResponse(studyChat);
    }

    //스터디 실시간 채팅 조회
    @Override
    public List<ChatResponse> findStudyChats(Integer studyId, Integer lastChatId){
        return studyChatRepository.findNewStudyChatsById(studyId, lastChatId == null ? 0 : lastChatId);
    }

    //이전 채팅 보기
    @Override
    public List<ChatResponse> findOldStudyChats(Integer studyId, Integer startChatId){
        PageRequest pageRequest = PageRequest.of(0, 100);
        return startChatId == null ? studyChatRepository.findOldStudyChats(studyId, pageRequest) : studyChatRepository.findOldStudyChatsByStartId(studyId, startChatId, pageRequest);
    }

    private boolean checkStudyMember(Integer studyId, Integer memberId){
        Optional<StudyMember> result = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId);
        return result.isPresent();
    }
}

package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.StudyChat;
import com.ssafy.interviewstudy.dto.study.ChatResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyChatRepository  extends JpaRepository<StudyChat, Integer> {

    @Query("select new com.ssafy.interviewstudy.dto.study.ChatResponse(sc.id, a.id, a.nickname, a.memberProfileBackground, a.memberProfileImage, sc.content, sc.createdAt) " +
            "from StudyChat sc join sc.author a where sc.study.id = :study_id and sc.id > :last_chat_id")
    public List<ChatResponse> findNewStudyChatsById(@Param("study_id") Integer studyId, @Param("last_chat_id")Integer lastChatId);

    @Query("select new com.ssafy.interviewstudy.dto.study.ChatResponse(sc.id, a.id, a.nickname, a.memberProfileBackground, a.memberProfileImage, sc.content, sc.createdAt) " +
            "from StudyChat sc join sc.author a where sc.study.id = :study_id order by sc.createdAt desc")
    public List<ChatResponse> findOldStudyChats(@Param("study_id") Integer studyId, Pageable pageable);

    @Query("select new com.ssafy.interviewstudy.dto.study.ChatResponse(sc.id, a.id, a.nickname, a.memberProfileBackground, a.memberProfileImage, sc.content, sc.createdAt) " +
            "from StudyChat sc join sc.author a where sc.study.id = :study_id and sc.id < :start_chat_id order by sc.createdAt desc")
    public List<ChatResponse> findOldStudyChatsByStartId(@Param("study_id") Integer studyId, @Param("start_chat_id") Integer startChatId, Pageable pageable);
}

package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.StudyChat;
import com.ssafy.interviewstudy.dto.study.ChatResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyChatRepository  extends JpaRepository<StudyChat, Integer> {

    @Query("select new com.ssafy.interviewstudy.dto.study.ChatResponse(sc.id, a.id, a.nickname, a.memberProfileBackground, a.memberProfileImage, sc.content, sc.createdAt) " +
            "from StudyChat sc left join sc.author a where sc.study.id = :study_id and sc.id > :last_chat_id")
    public List<ChatResponse> findNewStudyChatsById(@Param("study_id") Integer studyId, @Param("last_chat_id")Integer lastChatId);

    @Query("select new com.ssafy.interviewstudy.dto.study.ChatResponse(sc.id, a.id, a.nickname, a.memberProfileBackground, a.memberProfileImage, sc.content, sc.createdAt) " +
            "from StudyChat sc left join sc.author a where sc.study.id = :study_id order by sc.createdAt desc")
    public List<ChatResponse> findOldStudyChats(@Param("study_id") Integer studyId, Pageable pageable);

    @Query("select new com.ssafy.interviewstudy.dto.study.ChatResponse(sc.id, a.id, a.nickname, a.memberProfileBackground, a.memberProfileImage, sc.content, sc.createdAt) " +
            "from StudyChat sc left join sc.author a where sc.study.id = :study_id and sc.id < :start_chat_id order by sc.createdAt desc")
    public List<ChatResponse> findOldStudyChatsByStartId(@Param("study_id") Integer studyId, @Param("start_chat_id") Integer startChatId, Pageable pageable);


    //채팅을 친 회원이 탈퇴하거나 추방당했을 때
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update StudyChat sc set sc.author = null where sc.study.id = :studyId and sc.author.id = :authorId")
    public void deleteChatMemberByStudyIdAndAuthorId(@Param("studyId") Integer studyId, @Param("authorId") Integer authorId);
}

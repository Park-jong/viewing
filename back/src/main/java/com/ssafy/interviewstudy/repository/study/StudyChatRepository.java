package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyChat;
import com.ssafy.interviewstudy.dto.study.ChatResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyChatRepository  extends JpaRepository<StudyChat, Integer> {

    @Query("select new com.ssafy.interviewstudy.dto.study.ChatResponse(sc.id, a.id, a.nickname, a.memberProfileBackground, a.memberProfileImage, sc.content, sc.createdAt) " +
            "from StudyChat sc join sc.author a where sc.study.id = :study_id and sc.id > :last_chat_id")
    public List<ChatResponse> findNewStudyChatsById(@Param("study_id") Integer studyId, @Param("last_chat_id")Integer lastChatId);
}

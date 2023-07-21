package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyChatRepository  extends JpaRepository<StudyChat, Integer> {

    @Query("select sc from StudyChat sc where sc.study = :study and sc.id > :last_chat_id")
    public List<StudyChat> findNewStudyChats(@Param("study") Study study, @Param("last_chat_id")Integer lastChatId);
}

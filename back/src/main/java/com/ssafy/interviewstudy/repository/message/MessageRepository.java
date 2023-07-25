package com.ssafy.interviewstudy.repository.message;

import com.ssafy.interviewstudy.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    //보낸 쪽지함 조회
    @Query("select m from Message m inner join fetch m.author ma where ma.id = :authorId")
    List<Message> findMessagesByAuthorId(@Param("authorId") Integer authorId);

    //받은 쪽지함 조회
    @Query("select m from Message m inner join fetch m.receiver mr where mr.id = :receiverId")
    List<Message> findMessagesByReceiverId(Integer receiverId);

    //특정 쪽지함 자세히 보기
    @Query("select m from Message m inner join fetch m.author ma where m.id = :messageId")
    Message findMessageById(@Param("messageId") Integer messageId);

    @Modifying(clearAutomatically = true)
    Integer deleteMessageById(Integer messageId);
}

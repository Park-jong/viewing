package com.ssafy.interviewstudy.repository.message;

import com.ssafy.interviewstudy.domain.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    //보낸 쪽지함 조회
    List<Message> findMessagesByAuthorId(Integer authorId);

    //받은 쪽지함 조회
    List<Message> findMessagesByReceiverId(Integer receiverId);

    //특정 쪽지함 자세히 보기
    Message findMessageById(Integer messageId);
}

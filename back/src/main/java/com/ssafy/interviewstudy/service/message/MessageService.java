package com.ssafy.interviewstudy.service.message;

import com.ssafy.interviewstudy.dto.message.MessageCreatedResponse;
import com.ssafy.interviewstudy.dto.message.MessageDto;
import com.ssafy.interviewstudy.dto.message.MessageListResponse;
import com.ssafy.interviewstudy.dto.message.MessageSendRequest;
import org.springframework.transaction.annotation.Transactional;

public interface MessageService {
    //쪽지 상세 조회
    MessageDto getMessageDetail(Integer messageId);

    //보낸 쪽지들 조회
    MessageListResponse getSentMessages(Integer authorId);

    //받은 쪽지들 조회
    MessageListResponse getReceivedMessages(Integer receiverId);

    //쪽지 보내기
    MessageCreatedResponse sendMessage(MessageSendRequest messageSendRequest);

    Boolean checkMessageByMember(Integer messageId, Integer memberId);

    //쪽지 삭제
    Integer deleteMessage(Integer messageID);
}

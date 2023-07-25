package com.ssafy.interviewstudy.service.message;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.message.Message;
import com.ssafy.interviewstudy.dto.message.MessageListResponse;
import com.ssafy.interviewstudy.dto.message.MessageSendRequest;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.message.MessageRepository;
import com.ssafy.interviewstudy.dto.message.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    private final MemberRepository memberRepository;

    private final EntityManager em;


    @Autowired
    public MessageService(MessageRepository messageRepository, MemberRepository memberRepository, EntityManager em) {
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
        this.em = em;
    }



    //쪽지 상세 조회
    @Transactional
    public MessageDto getMessageDetail(Integer messageId) {
        Message message = messageRepository.findMessageById(messageId);
        message.readMessage();
        return MessageDto.fromEntity(message);
    }

    //보낸 쪽지들 조회
    @Transactional
    public MessageListResponse getSentMessages(Integer authorId) {
        List<Message> messageList = messageRepository.findMessagesByAuthorId(authorId);
        return MessageListResponse.fromEntityList(messageList);
    }

    //받은 쪽지들 조회
    @Transactional
    public MessageListResponse getReceivedMessages(Integer receiverId) {
        List<Message> messageList = messageRepository.findMessagesByReceiverId(receiverId);
        return MessageListResponse.fromEntityList(messageList);
    }

    //쪽지 보내기
    @Transactional
    public Integer sendMessage(MessageSendRequest messageSendRequest){
        Member author = memberRepository.findMemberById(messageSendRequest.getAuthorId());
        Member receiver = memberRepository.findMemberById(messageSendRequest.getReceiverId());
        Message message = MessageSendRequest.toEntity(messageSendRequest,author,receiver);
        messageRepository.save(message);
        return message.getId();
    }

    //쪽지 삭제
    @Transactional
    public Integer deleteMessage(Integer messageID){
        return messageRepository.deleteMessageById(messageID);
    }

}
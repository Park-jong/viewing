package com.ssafy.interviewstudy.service.message;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.message.Message;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.message.MessageCreatedResponse;
import com.ssafy.interviewstudy.dto.message.MessageListResponse;
import com.ssafy.interviewstudy.dto.message.MessageSendRequest;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.exception.member.MemberExceptionFactory;
import com.ssafy.interviewstudy.exception.message.CreationFailException;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.message.MessageRepository;
import com.ssafy.interviewstudy.dto.message.MessageDto;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    private final MemberRepository memberRepository;

    private final NotificationService notificationService;


    //쪽지 상세 조회
    @Override
    public MessageDto getMessageDetail(Integer messageId) {
        Message message = messageRepository.findMessageById(messageId);
        if(message==null) throw new NotFoundException("쪽지");
        message.readMessage();
        return MessageDto.fromEntity(message);
    }

    //보낸 쪽지들 조회
    @Override
    public MessageListResponse getSentMessages(Integer authorId) {
        List<Message> messageList = messageRepository.findMessagesByAuthorId(authorId);
        return MessageListResponse.fromEntityList(messageList);
    }

    //받은 쪽지들 조회
    @Override
    public MessageListResponse getReceivedMessages(Integer receiverId) {
        List<Message> messageList = messageRepository.findMessagesByReceiverId(receiverId);
        return MessageListResponse.fromEntityList(messageList);
    }

    //쪽지 보내기
    @Transactional
    @Override
    public MessageCreatedResponse sendMessage(MessageSendRequest messageSendRequest){
        Member author = memberRepository.findMemberById(messageSendRequest.getAuthorId()).orElseThrow(MemberExceptionFactory::memberNotFound);
        Member receiver = memberRepository.findMemberById(messageSendRequest.getReceiverId()).orElseThrow(MemberExceptionFactory::memberNotFound);
        if(author==null || receiver==null) throw new CreationFailException("쪽지");

        Message message = MessageSendRequest.toEntity(messageSendRequest,author,receiver);
        messageRepository.save(message);
        System.out.println(message);
        if(message.getId()==null) throw new CreationFailException("쪽지");

        notificationService.sendNotificationToMember(
                NotificationDto
                        .builder()
                        .memberId(messageSendRequest.getReceiverId())
                        .content("메시지가 도착했습니다! "+message.getTitle())
                        .notificationType(NotificationType.Message)
                        .url(message.getId().toString())
                        .isRead(false)
                        .build());

        return new MessageCreatedResponse(message.getId());
    }

    @Override
    public Boolean checkMessageByMember(Integer messageId, Integer memberId){
        Member member = memberRepository.findMemberById(memberId).orElseThrow(MemberExceptionFactory::memberNotFound);
        Message message = messageRepository.findMessageById(messageId);

        if(message==null || member==null) return false;

        if(message.getAuthor().getId().equals(memberId) || message.getReceiver().getId().equals(memberId)){
            return true;
        }
        return false;
    }

    //쪽지 삭제
    @Transactional
    @Override
    public Integer deleteMessage(Integer messageID){
        return messageRepository.deleteMessageById(messageID);
    }

}
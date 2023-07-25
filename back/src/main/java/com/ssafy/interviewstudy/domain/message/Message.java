package com.ssafy.interviewstudy.domain.message;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "message")
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id @GeneratedValue
    @Column(name = "message_id")
    private Integer id;

    private String title;

    @Column(length = 5000)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Boolean isRead=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Builder
    public Message(Integer id, String title, String content, LocalDateTime createdAt, Boolean isRead, Member author, Member receiver) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.author = author;
        this.receiver = receiver;
    }


    public void readMessage(){
        isRead = true;
    }
}

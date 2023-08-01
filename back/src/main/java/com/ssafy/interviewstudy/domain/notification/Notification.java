package com.ssafy.interviewstudy.domain.notification;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="url")
    private String url;

    @Column(name="content",length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @ColumnDefault("false")
    @Column(name = "is_read",insertable = false)
    private Boolean isRead;

    public void readNotification(){
         this.isRead = true;
    }

    @Builder
    public Notification(Integer id, LocalDateTime createdAt, String url, String content, Member author, NotificationType notificationType, Boolean isRead) {
        this.id = id;
        this.createdAt = createdAt;
        this.url = url;
        this.content = content;
        this.author = author;
        this.notificationType = notificationType;
        this.isRead = isRead;
    }
}

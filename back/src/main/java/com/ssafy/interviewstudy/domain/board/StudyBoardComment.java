package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "study_board_comment")
public class StudyBoardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_board_comment_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private StudyBoard article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ColumnDefault("false")
    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    //셀프 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_comment_id")
    private ArticleComment comment;

    @OneToMany(mappedBy = "comment")
    private List<ArticleComment> replies = new ArrayList<>();

    @Builder
    public StudyBoardComment(Integer id, StudyBoard article, Member author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDelete, ArticleComment comment, List<ArticleComment> replies) {
        this.id = id;
        this.article = article;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDelete = isDelete;
        this.comment = comment;
        this.replies = replies;
    }

    public void modifyComment(CommentRequest commentRequest) {
        this.content = commentRequest.getContent();
    }

    public void deleteComment(){
        this.isDelete = true;
    }
}

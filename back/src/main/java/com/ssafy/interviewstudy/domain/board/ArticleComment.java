package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.dto.Author;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Board article;
    @Embedded
    private Author author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDelete;

    //셀프 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_comment_id")
    private ArticleComment comment;

    @OneToMany(mappedBy = "comment")
    private List<ArticleComment> replies;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> likes;
}

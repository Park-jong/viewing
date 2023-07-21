package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.dto.Author;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "board_type")
public abstract class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Embedded
    private Author author;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @OneToMany(mappedBy = "article")
    private List<ArticleComment> comments;

    @OneToMany(mappedBy = "article")
    private List<ArticleLike> likes;

    @OneToMany(mappedBy = "article")
    private List<ArticleFile> files;

    @OneToMany(mappedBy = "article")
    private List<ReportArticle> reports;
}

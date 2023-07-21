package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.dto.Author;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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

package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
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
@Getter
@Entity
@NoArgsConstructor
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "board_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ColumnDefault("0")
    @Column(name = "view_count", insertable = false)
    private Integer viewCount;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ArticleComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ArticleLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ArticleFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ReportArticle> reports = new ArrayList<>();

    public void modifyArticle(BoardRequest boardRequest) {
        this.title = boardRequest.getTitle();
        this.content = boardRequest.getContent();
    }

    public void updateViewCount(){
        this.viewCount += 1;
    }

    @Builder
    public Board(Integer id, Member author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, BoardType boardType, Integer viewCount, List<ArticleComment> comments, List<ArticleLike> likes, List<ArticleFile> files, List<ReportArticle> reports) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.boardType = boardType;
        this.viewCount = viewCount;
        this.comments = comments;
        this.likes = likes;
        this.files = files;
        this.reports = reports;
    }

}

package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StudyBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

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

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<StudyBoardComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "studyArticle", cascade = CascadeType.REMOVE)
    private List<ArticleFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ReportArticle> reports = new ArrayList<>();

    public void modifyArticle(BoardRequest boardRequest) {
        this.title = boardRequest.getTitle();
        this.content = boardRequest.getContent();
    }

    @Builder
    public StudyBoard(Integer id, Study study, Member author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, List<StudyBoardComment> comments, List<ArticleFile> files, List<ReportArticle> reports) {
        this.id = id;
        this.study = study;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.comments = comments;
        this.files = files;
        this.reports = reports;
    }
}

package com.ssafy.interviewstudy.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "article_file")
public class ArticleFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_file_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Board article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_article_id")
    private StudyBoard studyArticle;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;
    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;
    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Builder
    public ArticleFile(Integer id, Board article, String originalFileName, byte[] fileData, String fileType) {
        this.id = id;
        this.article = article;
        this.originalFileName = originalFileName;
        this.fileData = fileData;
        this.fileType = fileType;
    }
}

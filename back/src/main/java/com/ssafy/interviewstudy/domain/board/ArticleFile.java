package com.ssafy.interviewstudy.domain.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "save_file_name", nullable = false)
    private String saveFileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    public ArticleFile(Board article, MultipartFile files, String saveFileName) {
        this.article = article;
        this.originalFileName = files.getOriginalFilename();
        this.fileType = files.getContentType();
        this.saveFileName = saveFileName;
    }

    public ArticleFile(StudyBoard studyArticle, MultipartFile files, String saveFileName) {
        this.studyArticle = studyArticle;
        this.originalFileName = files.getOriginalFilename();
        this.fileType = files.getContentType();
        this.saveFileName = saveFileName;
    }
}

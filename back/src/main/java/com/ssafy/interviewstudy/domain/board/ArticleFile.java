package com.ssafy.interviewstudy.domain.board;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
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


}

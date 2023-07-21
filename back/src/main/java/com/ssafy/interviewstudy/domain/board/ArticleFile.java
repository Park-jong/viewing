package com.ssafy.interviewstudy.domain.board;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "article_file")
public class ArticleFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Board article;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;
    @Lob
    @Column(name = "file", columnDefinition="BLOB", nullable = false)
    private byte[] file;
    @Column(name = "file_type", nullable = false)
    private String fileType;
}

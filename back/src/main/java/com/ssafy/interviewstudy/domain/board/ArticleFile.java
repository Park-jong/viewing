package com.ssafy.interviewstudy.domain.board;

import javax.persistence.*;

@Entity
public class ArticleFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Board article;
    private String originalFileName;
    @Lob
    @Column(name = "file", columnDefinition="BLOB")
    private byte[] file;
    private String fileType;
}

package com.ssafy.interviewstudy.domain.board;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ReportArticle {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Board article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String reasonDirect;
    private LocalDateTime reportedAt;

    @OneToMany(mappedBy = "reportArticle")
    private List<ReportReason> reasons;
}

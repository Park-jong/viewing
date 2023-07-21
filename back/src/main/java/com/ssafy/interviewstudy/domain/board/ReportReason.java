package com.ssafy.interviewstudy.domain.board;

import javax.persistence.*;

@Entity
public class ReportReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private ReportArticle reportArticle;

    private String description;
}

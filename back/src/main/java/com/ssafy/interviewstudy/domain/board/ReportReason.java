package com.ssafy.interviewstudy.domain.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "report_reason")
public class ReportReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_reason_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private ReportArticle reportArticle;

    @Column(name = "description", nullable = false)
    private String description;

    @Builder
    public ReportReason(Integer id, ReportArticle reportArticle, String description) {
        this.id = id;
        this.reportArticle = reportArticle;
        this.description = description;
    }
}

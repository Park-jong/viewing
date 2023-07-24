package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.sql.In;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
@NoArgsConstructor
@Table(name = "report_article")
public class ReportArticle {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "report_article_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Board article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "reason_direct")
    private String reasonDirect;
    @Column(name = "reported_at", nullable = false)
    @CreatedDate
    private LocalDateTime reportedAt;

    @OneToMany(mappedBy = "reportArticle")
    private List<ReportReason> reasons = new ArrayList<>();

    @Builder
    public ReportArticle(Integer id, Board article, Member member, String reasonDirect, LocalDateTime reportedAt, List<ReportReason> reasons) {
        this.id = id;
        this.article = article;
        this.member = member;
        this.reasonDirect = reasonDirect;
        this.reportedAt = reportedAt;
        this.reasons = reasons;
    }
}

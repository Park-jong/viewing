package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ReportArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportArticleRepository extends JpaRepository<ReportArticle, Integer> {
}

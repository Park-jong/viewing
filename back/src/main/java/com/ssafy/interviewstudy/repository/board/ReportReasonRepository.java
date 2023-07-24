package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ReportReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportReasonRepository extends JpaRepository<ReportReason, Integer> {
}

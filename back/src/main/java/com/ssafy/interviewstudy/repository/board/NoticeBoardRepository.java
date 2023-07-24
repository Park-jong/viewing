package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Integer> {
    Page<NoticeBoard> findAll(Pageable pageable);
}

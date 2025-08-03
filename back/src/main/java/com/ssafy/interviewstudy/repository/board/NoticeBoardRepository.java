package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.NoticeBoard;
import com.ssafy.interviewstudy.repository.CommonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeBoardRepository extends CommonRepository<NoticeBoard, Integer> {
    Page<NoticeBoard> findAll(Pageable pageable);
}

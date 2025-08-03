package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.NoticeBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeBoardRepositoryImpl implements NoticeBoardRepository {

    private final JPANoticeBoardRepository jpaNoticeBoardRepository;

    @Override
    public Optional<NoticeBoard> findById(Integer id) {
        return jpaNoticeBoardRepository.findById(id);
    }

    @Override
    public NoticeBoard save(NoticeBoard noticeBoard) {
        return jpaNoticeBoardRepository.save(noticeBoard);
    }

    @Override
    public void delete(NoticeBoard noticeBoard) {
        jpaNoticeBoardRepository.delete(noticeBoard);
    }

    @Override
    public void deleteById(Integer id) {
        jpaNoticeBoardRepository.deleteById(id);
    }

    @Override
    public Page<NoticeBoard> findAll(Pageable pageable) {
        return jpaNoticeBoardRepository.findAll(pageable);
    }
}

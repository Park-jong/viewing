package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

    private final JPABoardRepository jpaBoardRepository;

    @Override
    public Optional<Board> findById(Integer id) {
        return jpaBoardRepository.findById(id);
    }

    @Override
    public Board save(Board board) {
        return jpaBoardRepository.save(board);
    }

    @Override
    public void delete(Board board) {
        jpaBoardRepository.delete(board);
    }

    @Override
    public void deleteById(Integer id) {
        jpaBoardRepository.findById(id);
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return jpaBoardRepository.findAll(pageable);
    }

    @Override
    public int updateViewCount(Integer id) {
        return jpaBoardRepository.updateViewCount(id);
    }

    @Override
    public Page<Board> findByTitleContaining(String keyword, BoardType boardType, Pageable pageable) {
        return jpaBoardRepository.findByTitleContaining(keyword, boardType, pageable);
    }

    @Override
    public Page<Board> findByTitleOrContent(String keyword, BoardType boardType, Pageable pageable) {
        return jpaBoardRepository.findByTitleOrContent(keyword, boardType, pageable);
    }

    @Override
    public Page<Board> findWithAuthor(String keyword, BoardType boardType, Pageable pageable) {
        return jpaBoardRepository.findWithAuthor(keyword, boardType, pageable);
    }

    @Override
    public Page<Board> findAllByMemberId(Integer memberId, Pageable pageable) {
        return jpaBoardRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    public Page<Board> findByMemberIdAndBoardType(Integer memberId, BoardType boardType, Pageable pageable) {
        return jpaBoardRepository.findByMemberIdAndBoardType(memberId, boardType, pageable);
    }

    @Override
    public Page<Board> findByType(BoardType boardType, Pageable pageable) {
        return jpaBoardRepository.findByType(boardType, pageable);
    }

    @Override
    public List<Board> findAllByMember(Member member) {
        return jpaBoardRepository.findAllByMember(member);
    }
}

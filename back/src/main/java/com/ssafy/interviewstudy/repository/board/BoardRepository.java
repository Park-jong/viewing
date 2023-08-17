package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findAll(Pageable pageable);

    // 조회수 업데이트
    @Transactional
    @Modifying
    @Query("update Board b set b.viewCount = b.viewCount + 1 where b.id = :id ")
    int updateViewCount(Integer id);

    // 검색조건 : 제목 제목+내용 작성자
    @Query("select b from Board b JOIN b.author ba where b.title like %:keyword% and b.boardType = :boardType order by b.createdAt desc")
    Page<Board> findByTitleContaining(@Param("keyword") String keyword, @Param("boardType") BoardType boardType, Pageable pageable);

    // 제목+내용
    @Query("select b from Board b JOIN b.author ba where b.title like %:keyword% or b.content like %:keyword% and b.boardType = :boardType order by b.createdAt desc")
    Page<Board> findByTitleOrContent(@Param("keyword") String keyword, BoardType boardType, Pageable pageable);

    // 작성자
    @Query("SELECT b FROM Board b JOIN b.author ba where ba.nickname like %:keyword% and b.boardType = :boardType order by b.createdAt desc")
    Page<Board> findWithAuthor(@Param("keyword") String keyword, BoardType boardType, Pageable pageable);

    // 내가 쓴 모든 글 조회
    @Query("SELECT b FROM Board b JOIN b.author ba where ba.id = :memberId order by b.createdAt desc")
    Page<Board> findAllByMemberId(Integer memberId, Pageable pageable);

    // 내가 쓴 글 조회(게시판 별로)
    @Query("select b from Board b JOIN b.author ba where ba.id = :memberId and b.boardType = :boardType order by b.createdAt desc")
    Page<Board> findByMemberIdAndBoardType(Integer memberId, BoardType boardType, Pageable pageable);

    // 글 리스트 조회
    @Query("select b from Board b JOIN b.author ba where b.boardType = :boardType order by b.createdAt desc")
    Page<Board> findByType(BoardType boardType, Pageable pageable);

    //내가 쓴 글 (탈퇴용)
    @Query("SELECT b FROM Board b where b.author = :member")
    List<Board> findAllByMember(Member member);
}

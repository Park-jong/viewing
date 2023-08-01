package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

public interface MemberCommentRepository extends JpaRepository<Board,Integer>{
    @Query("select b from Board b inner join b.comments c where c.author.id=:id and b.boardType = :boardType")
    public List<Board> getCommentedBoardByMemberId(@Param("id") Integer memberId, @Param("boardType")BoardType boardType);
}

package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.board.ArticleLikeRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardDtoService boardDtoService;
    private final ArticleLikeRepository articleLikeRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    //글 리스트 조회, crud, 검색, 댓글 crud, 글 좋아요, 댓글 좋아요, 글 신고

    //글 목록 조회
    public List<BoardResponse> findBoardList(BoardType boardType, Pageable pageable) {
        List<Board> boardList = boardRepository.findByType(boardType, pageable).getContent();
        List<BoardResponse> responseList = new ArrayList<>();

        for (Board b : boardList) {
            responseList.add(boardDtoService.fromEntityWithoutContent(b));
        }

        return responseList;
    }

    // 글 detail 조회
    public BoardResponse findArticle(Integer memberId, Integer articleId, BoardType boardType) {
        Board article = boardRepository.findById(articleId).get();

        if(article != null) modifyViewCount(article);
        else System.out.println("null입니다...");

        // Null이면 예외 발생 처리
        BoardResponse boardResponse = boardDtoService.fromEntity(memberId, article);
        boardResponse.setBoardType(boardType);

        return boardResponse;
    }

    // 글 수정
    public BoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest){
        Board originArticle = boardRepository.findById(articleId).get();
        originArticle.modifyArticle(boardRequest);
        boardRepository.save(originArticle);

        return boardDtoService.fromEntity(boardRequest.getMemberId(), originArticle);
    }

    // 글 삭제
    public Integer removeArticle(Integer articleId){
        if(boardRepository.findById(articleId) == null){
            return 0;
        }

        boardRepository.deleteById(articleId);
        return articleId;
    }

    // 글 저장
    public Integer saveBoard(BoardRequest boardRequest){
        Board article = boardRepository.save(boardDtoService.toEntity(boardRequest));
        return article.getId();
    }

    // 글 검색
    public List<BoardResponse> findArticleByKeyword(String searchBy, String keyword, BoardType boardType, Pageable pageable){
        List<Board> articles;
        List<BoardResponse> responseList = new ArrayList<>();
        if(searchBy.equals("title")) articles = boardRepository.findByTitleContaining(keyword, boardType, pageable).getContent();
        else if(searchBy.equals("content")) articles = boardRepository.findByTitleOrContent(keyword, boardType, pageable).getContent();
        else articles = boardRepository.findWithAuthor(keyword, boardType, pageable).getContent();

        for (Board b: articles) {
            responseList.add(boardDtoService.fromEntityWithoutContent(b));
        }

        return responseList;
    }

    // 조회수+1
    public void modifyViewCount(Board article){
        article.updateViewCount();
        boardRepository.save(article);
    }


    // 글 작성자가 본인인지 아닌지 체크
    public Boolean checkAuthor(Integer articleId, Integer memberId){
        Board article = boardRepository.findById(articleId).get();

        // 본인이면 true, 아니면 false
        if(article.getAuthor().getId() == memberId) return true;
        else return false;
    }

    // 좋아요 누르기
    public Integer saveArticleLike(Integer memberId, Integer articleId){
        Member member = memberRepository.findMemberById(memberId);
        Board article = boardRepository.findById(articleId).get();

        if(checkMemberLikeArticle(memberId, articleId)) return 0;

        ArticleLike articleLike = articleLikeRepository.save(ArticleLike.builder()
                .member(member)
                .article(article)
                .build());

        return articleLike.getId();
    }

    // 좋아요 삭제
    public void removeArticleLike(Integer memberId, Integer articleId){
        Member member = memberRepository.findMemberById(memberId);
        Board article = boardRepository.findById(articleId).get();

        if(checkMemberLikeArticle(memberId, articleId)){
            articleLikeRepository.removeByArticleAndMember(article, member);
        }
    }

    // 유저가 좋아요를 한 상태면 true, 아니면 false
    public Boolean checkMemberLikeArticle(Integer memberId, Integer articleId){
        return articleLikeRepository.existsByMember_IdAndArticle_Id(memberId, articleId);
    }

}

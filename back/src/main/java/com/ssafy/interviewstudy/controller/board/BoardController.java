package com.ssafy.interviewstudy.controller.board;

import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.board.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/boards")
@RestController
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    //@RequestAttribute JWTMemberInfo memberInfo

    @GetMapping("/{boardType}/{articleId}")
    public ResponseEntity<?> articleDetail(@MemberInfo JWTMemberInfo memberInfo, @PathVariable BoardType boardType, @PathVariable Integer articleId){
        BoardResponse boardResponse = boardService.findArticle(memberInfo.getMemberId(), articleId, boardType);

        return ResponseEntity.ok(boardResponse);
    }

    // 글 저장
    @PostMapping("/{boardType}")
    public ResponseEntity<?> articleAdd(@PathVariable BoardType boardType, @RequestBody BoardRequest boardRequest){
        Integer articleId = boardService.saveBoard(boardRequest);

        return ResponseEntity.ok(articleId);
    }

    @PutMapping("/{boardType}/{articleId}")
    public ResponseEntity<?> articleModify(@PathVariable Integer articleId, @RequestBody BoardRequest boardRequest){
        BoardResponse response = boardService.modifyArticle(articleId, boardRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{boardType}/{articleId}")
    public ResponseEntity<?> articleRemove(@PathVariable Integer articleId){
        Integer response = boardService.removeArticle(articleId);
        if(response == 0)
            return ResponseEntity.badRequest().body("없는 게시물입니다.");

        return ResponseEntity.ok(response);
    }

    // 글 검색(키워드 없으면 전체 글 조회
    @GetMapping("/{boardType}")
    public ResponseEntity<?> articleList(@RequestParam("searchBy") String searchBy, @RequestParam("keyword") String keyword, @PathVariable BoardType boardType, Pageable pageable){
        List<BoardResponse> boardResponses;
        if(StringUtils.hasText(keyword))
            boardResponses = boardService.findArticleByKeyword(searchBy, keyword, boardType, pageable);
        else boardResponses = boardService.findBoardList(boardType, pageable);

        return ResponseEntity.ok(boardResponses);
    }

}

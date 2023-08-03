package com.ssafy.interviewstudy.controller.board;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/boards")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @JWTRequired
    @GetMapping("/{boardType}/{articleId}")
    public ResponseEntity<?> articleDetail(@MemberInfo JWTMemberInfo memberInfo, @PathVariable BoardType boardType, @PathVariable Integer articleId){
        Integer memberId = (memberInfo == null) ? null : memberInfo.getMemberId();
        BoardResponse boardResponse = boardService.findArticle(memberId, articleId, boardType);

        return ResponseEntity.ok(boardResponse);
    }

    // 글 저장
    @JWTRequired(required = true)
    @PostMapping("/{boardType}")
    public ResponseEntity<?> articleAdd(@PathVariable BoardType boardType,
                                        @MemberInfo JWTMemberInfo memberInfo,
                                        @RequestBody BoardRequest boardRequest){
        boardRequest.setBoardType(boardType);
        boardRequest.setMemberId(memberInfo.getMemberId());
        Integer articleId = boardService.saveBoard(boardRequest);

        return ResponseEntity.ok(articleId);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Board)
    @PutMapping("/{boardType}/{articleId}")
    public ResponseEntity<?> articleModify(@PathVariable BoardType boardType, @PathVariable Integer articleId,
                                           @MemberInfo JWTMemberInfo memberInfo,
                                           @RequestBody BoardRequest boardRequest){
        boardRequest.setBoardType(boardType);
        boardRequest.setMemberId(memberInfo.getMemberId());
        BoardResponse response = boardService.modifyArticle(articleId, boardRequest);

        return ResponseEntity.ok(response);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Board)
    @DeleteMapping("/{boardType}/{articleId}")
    public ResponseEntity<?> articleRemove(@PathVariable Integer articleId){
        Integer response = boardService.removeArticle(articleId);
        if(response == 0)
            return ResponseEntity.badRequest().body("없는 게시물입니다.");

        return ResponseEntity.ok(response);
    }

    // 글 검색(키워드 없으면 전체 글 조회
    @GetMapping("/{boardType}")
    public ResponseEntity<?> articleList(@RequestParam(value = "searchBy", required = false) String searchBy, @RequestParam(value = "keyword", required = false) String keyword, @PathVariable BoardType boardType, Pageable pageable){
        List<BoardResponse> boardResponses;
        if(StringUtils.hasText(keyword))
            boardResponses = boardService.findArticleByKeyword(searchBy, keyword, boardType, pageable);
        else boardResponses = boardService.findBoardList(boardType, pageable);

        return ResponseEntity.ok(boardResponses);
    }

}

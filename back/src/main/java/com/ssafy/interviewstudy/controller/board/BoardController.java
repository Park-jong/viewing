package com.ssafy.interviewstudy.controller.board;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.study.RequestFile;
import com.ssafy.interviewstudy.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RequestMapping("/boards/{boardType}")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 글 상세조회(하나)
    @JWTRequired
    @GetMapping("/{articleId}")
    public ResponseEntity<?> articleDetail(@MemberInfo JWTMemberInfo memberInfo, @PathVariable BoardType boardType, @PathVariable Integer articleId){
        Integer memberId = (memberInfo == null) ? null : memberInfo.getMemberId();
        BoardResponse boardResponse = boardService.findArticle(memberId, articleId, boardType);

        return ResponseEntity.ok(boardResponse);
    }

    // 글 저장
    @JWTRequired(required = true)
    @PostMapping
    public ResponseEntity<?> articleAdd(@PathVariable BoardType boardType,
                                        @MemberInfo JWTMemberInfo memberInfo,
                                        @RequestBody BoardRequest boardRequest,
                                        @RequestPart(value = "request_files", required = false)List<MultipartFile> requestFiles){
        boardRequest.setBoardType(boardType);
        boardRequest.setMemberId(memberInfo.getMemberId());
        Integer articleId = boardService.saveBoard(boardRequest, requestFiles);

        return ResponseEntity.ok(articleId);
    }

    // 글 수정
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Board)
    @PutMapping("/{articleId}")
    public ResponseEntity<?> articleModify(@PathVariable BoardType boardType, @PathVariable Integer articleId,
                                           @MemberInfo JWTMemberInfo memberInfo,
                                           @RequestBody BoardRequest boardRequest,
                                           @RequestPart(value = "request_files", required = false)List<MultipartFile> requestFiles){
        boardRequest.setBoardType(boardType);
        boardRequest.setMemberId(memberInfo.getMemberId());
        BoardResponse response = boardService.modifyArticle(articleId, boardRequest, requestFiles);

        return ResponseEntity.ok(response);
    }

    // 글 삭제
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Board)
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> articleRemove(@PathVariable Integer articleId){
        Integer response = boardService.removeArticle(articleId);
        if(response == 0)
            return ResponseEntity.badRequest().body("없는 게시물입니다.");

        return ResponseEntity.ok(response);
    }

    // 글 검색(키워드 없으면 전체 글 조회)
    @GetMapping
    public ResponseEntity<?> articleList(@RequestParam(value = "searchBy", required = false) String searchBy,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         @PathVariable BoardType boardType, Pageable pageable){
        List<BoardResponse> boardResponses;
        if(StringUtils.hasText(keyword))
            boardResponses = boardService.findArticleByKeyword(searchBy, keyword, boardType, pageable);
        else boardResponses = boardService.findBoardList(boardType, pageable);

        return ResponseEntity.ok(boardResponses);
    }

    // 파일 다운로드
    @GetMapping("/{articleId}/files/{fileId}")
    public ResponseEntity<?> articleFile(@PathVariable Integer boardType, @PathVariable Integer articleId, @PathVariable Integer fileId){
        FileResponse file = boardService.fileDownload(fileId);

        HttpHeaders httpHeaders = new HttpHeaders();
        String fileName = null;
        try {
            fileName = URLEncoder.encode(file.getName(), "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        httpHeaders.setContentType(MediaType.parseMediaType(file.getFileType()));
        httpHeaders.setContentLength(file.getFileData().length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(file.getFileData(), httpHeaders, HttpStatus.OK);
    }

    // 파일 삭제
    @JWTRequired(required = true)
    @DeleteMapping("/{articleId}/files")
    public ResponseEntity<?> articleFile(@PathVariable Integer boardType, @PathVariable Integer articleId){
        boardService.removeFiles(articleId);

        return ResponseEntity.ok().build();
    }

}

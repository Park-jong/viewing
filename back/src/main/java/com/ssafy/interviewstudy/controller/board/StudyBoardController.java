package com.ssafy.interviewstudy.controller.board;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.dto.board.StudyBoardResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.board.StudyBoardService;
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

@RequestMapping("/studies/{studyId}/boards")
@RestController
@RequiredArgsConstructor
public class StudyBoardController {

    private final StudyBoardService boardService;

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Study_Article)
    @GetMapping("/{articleId}")
    public ResponseEntity<?> articleDetail(@PathVariable Integer articleId){
        StudyBoardResponse boardResponse = boardService.findArticle(articleId);

        return ResponseEntity.ok(boardResponse);
    }

    // 글 저장
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Study_Member)
    @PostMapping
    public ResponseEntity<?> articleAdd(@PathVariable Integer studyId, @MemberInfo JWTMemberInfo memberInfo,
                                        @RequestPart(value = "request", required = false) BoardRequest boardRequest,
                                        @RequestPart(value = "request_files", required = false) List<MultipartFile> requestFiles){
        boardRequest.setMemberId(memberInfo.getMemberId());
        System.out.println(studyId);
        boardRequest.setStudyId(studyId);
        Integer articleId = boardService.saveBoard(boardRequest, requestFiles);

        return ResponseEntity.ok(articleId);
    }

//    @Authority(authorityType = AuthorityType.Member_Board)
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Study_Article)
    @PutMapping("/{articleId}")
    public ResponseEntity<?> articleModify(@PathVariable Integer studyId, @PathVariable Integer articleId,
                                           @MemberInfo JWTMemberInfo memberInfo,
                                           @RequestPart(value = "request", required = false) BoardRequest boardRequest,
                                           @RequestPart(value = "request_files", required = false)List<MultipartFile> requestFiles){
        boardRequest.setMemberId(memberInfo.getMemberId());
        boardRequest.setStudyId(studyId);
        // 삭제된 파일의 리스트를 받아서 서버와 db에서 삭제
        if (boardRequest.getFilesDeleted()!= null) boardService.removeFileList(boardRequest.getFilesDeleted());
        StudyBoardResponse response = boardService.modifyArticle(articleId, boardRequest, requestFiles);

        return ResponseEntity.ok(response);
    }

//    @Authority(authorityType = AuthorityType.Member_Board)
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Study_Article)
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> articleRemove(@PathVariable Integer articleId){
        Integer response = boardService.removeArticle(articleId);

        if(response == 0)
            return ResponseEntity.badRequest().body("없는 게시물입니다.");

        return ResponseEntity.ok(response);
    }

    // 글 검색(키워드 없으면 전체 글 조회
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Study_Member)
    @GetMapping
    public ResponseEntity<?> articleList(@PathVariable Integer studyId, @RequestParam(value = "searchBy", required = false) String searchBy,
                                         @RequestParam(value = "keyword", required = false) String keyword, Pageable pageable){
        List<StudyBoardResponse> boardResponses;
        if(StringUtils.hasText(keyword))
            boardResponses = boardService.findArticleByKeyword(studyId, searchBy, keyword, pageable);
        else boardResponses = boardService.findBoardList(studyId, pageable);

        return ResponseEntity.ok(boardResponses);
    }

    // 파일 다운로드
    @GetMapping("/{articleId}/files/{fileId}")
    public ResponseEntity<?> articleFile(@PathVariable Integer articleId, @PathVariable Integer fileId){
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
    public ResponseEntity<?> articleFile(@PathVariable Integer articleId){
        boardService.removeFiles(articleId);

        return ResponseEntity.ok().build();
    }

}

package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.redis.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardDtoManger {

    private final ArticleLikeService articleLikeService;
    private final MemberRepository memberRepository;

    public BoardResponse fromEntityWithoutContent(Board article) {
        int likeCount = articleLikeService.getLikeCount(article.getId());
        return BoardResponse.builder()
                .articleId(article.getId())
                .author(new Author(article.getAuthor()))
                .title(article.getTitle())
                .viewCount(article.getViewCount())
                .commentCount(article.getComments().size())
                .likeCount(likeCount)
                .build();
    }

    public BoardResponse fromEntityToResponse(Integer memberId, Board article) {
        BoardResponse boardResponse = fromEntityWithoutContent(article);
        if (memberId != null) {
            boardResponse.setIsLike(articleLikeService.checkMemberLikeArticle(article.getId(), memberId));
        }
        boardResponse.setContent(article.getContent());
        boardResponse.setTitle(article.getTitle());
        boardResponse.setCreatedAt(article.getCreatedAt());
        boardResponse.setUpdatedAt(article.getUpdatedAt());
        boardResponse.setBoardType(article.getBoardType());
        return boardResponse;
    }

    public void setFiles(BoardResponse response, List<FileResponse> files){
        response.setArticleFiles(files);
    }

    public Board fromRequestToEntity(BoardRequest boardRequest) {
        Member author = memberRepository.findMemberById(boardRequest.getMemberId());
        return Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .author(author)
                .boardType(boardRequest.getBoardType())
                .build();
    }
}

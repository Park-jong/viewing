package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.redis.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardDtoService {
    private final ArticleLikeService articleLikeService;

    public BoardResponse fromEntityWithoutContent(Board article) {
        return BoardResponse.builder()
                .articleId(article.getId())
                .author(new Author(article.getAuthor()))
                .title(article.getTitle())
                .viewCount(article.getViewCount())
                .commentCount(article.getComments().size())
                .likeCount(articleLikeService.getLikeCount(article.getId()))
                .build();
    }

}

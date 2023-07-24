package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("interview_review_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class InterviewReviewBoard extends Board{
    @Builder
    public InterviewReviewBoard(Integer id, Member author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount) {
        super(id, author, title, content, createdAt, updatedAt, viewCount);
    }
}

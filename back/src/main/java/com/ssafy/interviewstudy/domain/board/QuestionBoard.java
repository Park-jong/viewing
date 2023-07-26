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
@DiscriminatorValue("question_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class QuestionBoard extends Board{
    @Builder
    public QuestionBoard(Integer id, Member author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, author, title, content, createdAt, updatedAt);
    }
}

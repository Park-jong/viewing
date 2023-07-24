package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("free_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class FreeBoard extends Board {

    @Builder
    public FreeBoard(Integer id, Member author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount) {
        super(id, author, title, content, createdAt, updatedAt, viewCount);
    }
}

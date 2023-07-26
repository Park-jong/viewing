package com.ssafy.interviewstudy.domain.board;

import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("free_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class FreeBoard extends Board {
}

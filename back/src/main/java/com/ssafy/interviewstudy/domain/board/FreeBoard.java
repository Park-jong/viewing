package com.ssafy.interviewstudy.domain.board;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@DiscriminatorValue("free_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class FreeBoard extends Board {
}

package com.ssafy.interviewstudy.domain.board;

import javax.persistence.*;


@Entity
@DiscriminatorValue("free_board")
public class FreeBoard extends Board {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private int articleId;
}

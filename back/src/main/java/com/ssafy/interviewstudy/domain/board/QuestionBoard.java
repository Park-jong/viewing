package com.ssafy.interviewstudy.domain.board;

import javax.persistence.*;

@Entity
@DiscriminatorValue("question_board")
public class QuestionBoard extends Board{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private int articleId;
}

package com.ssafy.interviewstudy.domain.board;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@DiscriminatorValue("question_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class QuestionBoard extends Board{
}

package com.ssafy.interviewstudy.domain.board;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@DiscriminatorValue("interview_review_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class InterviewReviewBoard extends Board{
}

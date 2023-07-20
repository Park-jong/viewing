package com.ssafy.interviewstudy.domain.board;

import javax.persistence.*;

@Entity
@DiscriminatorValue("interview_review_board")
public class InterviewReviewBoard extends Board{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private int articleId;
    private int studyId;
}

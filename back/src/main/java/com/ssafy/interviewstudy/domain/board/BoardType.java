package com.ssafy.interviewstudy.domain.board;

public enum BoardType {

    FreeBoard,
    QuestionBoard,
    InterviewReviewBoard,
    qna,
    review,
    general;


    @Override
    public String toString() {
        return name();
    }
}

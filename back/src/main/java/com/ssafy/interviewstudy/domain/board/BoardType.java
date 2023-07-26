package com.ssafy.interviewstudy.domain.board;

public enum BoardType {

    FreeBoard,
    QuestionBoard,
    InterviewReviewBoard;


    @Override
    public String toString() {
        return name();
    }
}

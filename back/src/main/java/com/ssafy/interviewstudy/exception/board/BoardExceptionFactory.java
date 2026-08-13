package com.ssafy.interviewstudy.exception.board;

import com.ssafy.interviewstudy.exception.message.NotFoundException;

public class BoardExceptionFactory {
    public static NotFoundException commentNotFound() {
        return new NotFoundException("해당하는 댓글이 존재하지 않습니다.");
    }

    public static NotFoundException articleNotFound() {
        return new NotFoundException("해당하는 게시글이 존재하지 않습니다.");
    }

    public static NotFoundException memberNotFound() {
        return new NotFoundException("해당하는 회원이 존재하지 않습니다.");
    }
}

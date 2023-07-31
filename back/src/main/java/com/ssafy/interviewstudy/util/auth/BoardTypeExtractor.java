package com.ssafy.interviewstudy.util.auth;

import com.ssafy.interviewstudy.domain.board.BoardType;

import java.util.ArrayList;
import java.util.List;

public class BoardTypeExtractor{
        public static BoardType extract(String uri){
            String[] subUris = uri.split("\\/");
            BoardType boardType = null;
            for(String s : subUris) {
                for(BoardType bt : BoardType.values()){
                    if(s.equals(bt.name())){
                        boardType = bt;
                    }
                }
            }
            return boardType;
        }
}

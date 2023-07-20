package com.ssafy.interviewstudy.domain.board;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StudyBoard {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private int articleId;
}

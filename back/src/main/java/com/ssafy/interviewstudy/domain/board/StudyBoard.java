package com.ssafy.interviewstudy.domain.board;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@DiscriminatorValue("study_board")
@PrimaryKeyJoinColumn(name = "article_id")
public class StudyBoard extends Board{
    @Column(name = "study_id", nullable = false)
    private int studyId;
}

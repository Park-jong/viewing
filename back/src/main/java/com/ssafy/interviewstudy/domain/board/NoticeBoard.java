package com.ssafy.interviewstudy.domain.board;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class NoticeBoard {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

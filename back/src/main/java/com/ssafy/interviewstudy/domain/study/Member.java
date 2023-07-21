package com.ssafy.interviewstudy.domain.study;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer id;

    private String nickname;

    @OneToMany(mappedBy = "member")
    private List<StudyMember> studies = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<StudyBookmark> studyBookmarks = new ArrayList<>();
}

package com.ssafy.interviewstudy.domain.study;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class StudyTagType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_tag_type_id")
    private Integer id;

    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<StudyTag> studyTags = new ArrayList<>();
}

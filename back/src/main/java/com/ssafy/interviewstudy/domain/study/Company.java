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
public class Company {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer id;

    private String name;

    //private Image logo
    @OneToMany(mappedBy = "appliedCompany")
    private List<Study> studies = new ArrayList<>();
}

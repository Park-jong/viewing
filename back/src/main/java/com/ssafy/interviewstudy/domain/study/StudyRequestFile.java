package com.ssafy.interviewstudy.domain.study;

import com.ssafy.interviewstudy.dto.study.RequestFile;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class StudyRequestFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_request_file_id")
    private Integer id;

    private String originalFileName;

    private byte[] fileData;

    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_request_id")
    private StudyRequest request;

    public StudyRequestFile(RequestFile requestFile, StudyRequest studyRequest){
        this.request = studyRequest;
        this.fileData = requestFile.getFileData();
        this.originalFileName = requestFile.getName();
    }
}

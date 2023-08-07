package com.ssafy.interviewstudy.domain.study;

import com.ssafy.interviewstudy.dto.study.RequestFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;

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

    private String saveFileName;

    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_request_id")
    private StudyRequest request;

    public StudyRequestFile(MultipartFile requestFile, StudyRequest studyRequest, String saveFileName){
        this.request = studyRequest;
        this.originalFileName = requestFile.getOriginalFilename();
        this.fileType = requestFile.getContentType();
        this.saveFileName = saveFileName;
    }
}

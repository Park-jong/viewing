package com.ssafy.interviewstudy.dto.study;

import com.ssafy.interviewstudy.domain.study.StudyRequestFile;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class RequestFile {
    private String name;
    private byte[] fileData;
    public RequestFile(StudyRequestFile file){
        this.name = file.getOriginalFileName();
        this.fileData = file.getFileData();
    }
}

package com.ssafy.interviewstudy.dto.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class FileResponse {

    private String name;
    private Integer fileId;
    private String fileType;
    private byte[] fileData;

    public FileResponse(ArticleFile file){
        this.name = file.getOriginalFileName();
        this.fileId = file.getId();
        this.fileType = file.getFileType();
    }
}

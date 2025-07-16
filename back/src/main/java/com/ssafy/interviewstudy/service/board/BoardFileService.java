package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardFileService {
    void saveFiles(BoardRequest boardRequest, Board article, List<MultipartFile> files);

    FileResponse fileDownload(Integer fileId);

    void removeFileList(List<FileResponse> files);

    void removeFiles(Integer articleId);
}

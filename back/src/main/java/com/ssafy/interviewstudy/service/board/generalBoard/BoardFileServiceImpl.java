package com.ssafy.interviewstudy.service.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.repository.board.generalBoard.ArticleFileRepository;
import com.ssafy.interviewstudy.support.file.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardFileServiceImpl implements BoardFileService {

    private final FileManager fm;
    private final ArticleFileRepository articleFileRepository;


    @Override
    public void saveFiles(BoardRequest boardRequest, Board article, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (MultipartFile file : files) {
            try {
                String saveFileName = makeSaveFileName(boardRequest);
                fm.upload(file.getInputStream(), saveFileName, file.getContentType(), file.getSize());
                ArticleFile articleFile = new ArticleFile(article, file, saveFileName);
                articleFileRepository.save(articleFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void saveFiles(BoardRequest boardRequest, StudyBoard article, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (MultipartFile file : files) {
            try {
                String saveFileName = makeSaveFileName(boardRequest);
                fm.upload(file.getInputStream(), saveFileName, file.getContentType(), file.getSize());
                ArticleFile articleFile = new ArticleFile(article, file, saveFileName);
                articleFileRepository.save(articleFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String makeSaveFileName(BoardRequest boardRequest) {
        return boardRequest.getMemberId() + "_" + System.currentTimeMillis();
    }

    @Override
    public FileResponse fileDownload(Integer fileId) {
        ArticleFile articleFile = articleFileRepository.findById(fileId).get();
        FileResponse result = new FileResponse(articleFile);
        try {
            byte[] file = fm.download(articleFile.getSaveFileName());
            result.setFileData(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void removeFileList(List<FileResponse> files) {
        for (FileResponse f : files) {
            ArticleFile file = articleFileRepository.findById(f.getFileId()).get();
            fm.delete(file.getSaveFileName());
            articleFileRepository.deleteById(file.getId());
        }
    }

    @Override
    public void removeFiles(Integer articleId) {
        List<ArticleFile> files = articleFileRepository.findByArticle_Id(articleId);
        for (ArticleFile file : files) {
            fm.delete(file.getSaveFileName());
            articleFileRepository.deleteById(file.getId());
        }
    }
}

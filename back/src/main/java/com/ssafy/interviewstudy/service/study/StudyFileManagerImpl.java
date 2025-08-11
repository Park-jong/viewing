package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.domain.study.StudyRequest;
import com.ssafy.interviewstudy.domain.study.StudyRequestFile;
import com.ssafy.interviewstudy.dto.study.RequestDto;
import com.ssafy.interviewstudy.dto.study.RequestFile;
import com.ssafy.interviewstudy.repository.study.StudyRequestFileRepository;
import com.ssafy.interviewstudy.support.file.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyFileManagerImpl implements StudyFileManager {

    private final StudyRequestFileRepository studyRequestFileRepository;
    private final FileManager fm;

    @Override
    public void saveFile(StudyRequest studyRequest, RequestDto requestDto, List<MultipartFile> files){
        if(files != null) {
            for (MultipartFile file : files) {
                try{
                    String saveFileName = requestDto.getMemberId()+ "_" + System.currentTimeMillis();
                    fm.upload(file.getInputStream(), saveFileName, file.getContentType(), file.getSize());
                    StudyRequestFile studyRequestFile = new StudyRequestFile(file, studyRequest, saveFileName);
                    studyRequestFileRepository.save(studyRequestFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public RequestFile download(Integer fileId){
        StudyRequestFile studyRequestFile = studyRequestFileRepository.findById(fileId).orElseThrow();
        byte[] file = null;
        RequestFile result = new RequestFile(studyRequestFile);
        try {
            file = fm.download(studyRequestFile.getSaveFileName());
            result.setFileData(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

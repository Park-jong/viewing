package com.ssafy.interviewstudy.controller.study;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.dto.message.MessageCreatedResponse;
import com.ssafy.interviewstudy.dto.message.MessageSendRequest;
import com.ssafy.interviewstudy.dto.study.StudyDtoRequest;
import com.ssafy.interviewstudy.dto.study.StudyDtoResponse;
import com.ssafy.interviewstudy.service.study.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/studies")
public class StudyController {
    private final StudyService studyService;

    @Autowired
    public StudyController(StudyService studyService){
        this.studyService = studyService;
    }

    //스터디 조회
    @GetMapping
    public ResponseEntity<?> getStudies(@Param("option") Boolean option, @Param("appliedCompany") Integer appliedCompany, @Param("appliedJob") String appliedJob, @Param("careerLevel")CareerLevel careerLevel, @PageableDefault(size = 20)Pageable pageable){
        Page<StudyDtoResponse> page = studyService.findStudiesBySearch(option, appliedCompany, appliedJob, careerLevel, pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/{study_id}")
    public ResponseEntity<?> getStudy(@PathVariable("study_id") int studyId){
        StudyDtoResponse study = studyService.findStudyById(studyId);
        return ResponseEntity.ok().body(study);
    }

    @PostMapping
    public ResponseEntity<?> studySave(@Valid @RequestBody StudyDtoRequest study){
        Integer madeStudy = null;
        try{
            madeStudy = studyService.addStudy(study);
        }
        catch(ConstraintViolationException ce){
            return ResponseEntity.internalServerError().body("스터디 생성 실패");
        }
        return ResponseEntity.created(URI.create("/studies/"+madeStudy)).build();
    }

    @DeleteMapping("/{study_id}")
    public ResponseEntity<?> deleteStudy(@PathVariable("study_id") Integer studyId){
        studyService.removeStudy(studyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{study_id}")
    public ResponseEntity<?> modifyStudy(@PathVariable("study_id") Integer studyId, @Valid @RequestBody StudyDtoRequest study){
        try {
            studyService.modifyStudy(studyId, study);
        }catch(ConstraintViolationException ce){
            return ResponseEntity.internalServerError().body("스터디 수정 실패");
        }
        return ResponseEntity.created(URI.create("/studies/"+studyId)).build();
    }
}

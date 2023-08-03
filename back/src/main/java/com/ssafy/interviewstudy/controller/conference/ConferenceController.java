package com.ssafy.interviewstudy.controller.conference;

import com.ssafy.interviewstudy.service.study.StudyService;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@PropertySource("classpath:openvidu.properties")
public class ConferenceController {

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL,OPENVIDU_SECRET);
    }

    private final StudyService studyService;

    @PostMapping("/studies/{study_id}/conference")
    public ResponseEntity<String> initializeSession(@PathVariable(name="study_id") String studyId, @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        
//        Integer memberId = Integer.parseInt((String) params.get("user_id"));
//        Integer istudyId = Integer.parseInt(studyId);
//
//        if (!studyService.checkStudyMember(istudyId, memberId)) {
//            return new ResponseEntity<>("스터디 멤버가 아닙니다.", HttpStatus.UNAUTHORIZED);
//        };

        SessionProperties properties = SessionProperties.fromJson(params).build();
        Session checkSession = openvidu.getActiveSession(studyId);
        if (checkSession != null) {
            return new ResponseEntity<>(checkSession.getSessionId(), HttpStatus.OK);
        }
        Session session = openvidu.createSession(properties);
        return new ResponseEntity<>(session.getSessionId(),HttpStatus.OK);
    }

    @PostMapping("/studies/{study_id}/conferences/members")
    public ResponseEntity<String> createConnection(@PathVariable("study_id") String studyId,
                                                   @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {

//        Integer memberId = Integer.parseInt((String) params.get("user_id"));
//        Integer istudyId = Integer.parseInt(studyId);
//
//        if (!studyService.checkStudyMember(istudyId, memberId)) {
//            return new ResponseEntity<>("스터디 멤버가 아닙니다.", HttpStatus.UNAUTHORIZED);
//        };

        Session session = openvidu.getActiveSession(studyId);
        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
        Connection connection = null;
        try {
            connection = session.createConnection(properties);
        } catch (OpenViduHttpException e) {
            SessionProperties sessionProperties = SessionProperties.fromJson(params).build();
            openvidu.createSession(sessionProperties);
            connection = session.createConnection(properties);
        }

        return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
    }


}

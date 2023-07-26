package com.ssafy.interviewstudy.controller.conference;


import com.ssafy.interviewstudy.dto.conference.ConferenceRequest;
import com.ssafy.interviewstudy.dto.conference.ConferenceResponse;
import com.ssafy.interviewstudy.service.conference.ConferenceService;
import io.openvidu.java.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
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

    ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }


    @PostMapping("/studies/{study_id}/conference")
    private ResponseEntity<?> initializeSession(@PathVariable(name="study_id") int studyId, @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(params).build();
        Session session = openvidu.createSession(properties);

        ConferenceRequest conferenceRequest = new ConferenceRequest(studyId, (Integer) params.get("user_id"));
        conferenceService.createConference(conferenceRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/studies/{study_id}/conferences/members")
    public ResponseEntity<ConferenceResponse> createConnection(@PathVariable("study_id") int studyId,
                                                   @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(String.valueOf(studyId));
        if (session == null) {
            this.initializeSession(Integer.parseInt(String.valueOf(studyId)), params);
        }
        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
        Connection connection = session.createConnection(properties);
        ConferenceRequest conferenceRequest = new ConferenceRequest((Integer) params.get("conference_room_id"), studyId, (Integer) params.get("user_id"));
        ConferenceResponse conferenceResponse = conferenceService.joinConference(conferenceRequest);
        conferenceResponse.setSessionId(session.getSessionId());
        conferenceResponse.setToken(connection.getToken());
        return new ResponseEntity<>(conferenceResponse, HttpStatus.OK);
    }

}

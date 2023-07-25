package com.ssafy.interviewstudy.service.conference;

import com.ssafy.interviewstudy.domain.conference.ConferenceAttendee;
import com.ssafy.interviewstudy.domain.conference.ConferenceRoom;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.conference.ConferenceExitRequest;
import com.ssafy.interviewstudy.dto.conference.ConferenceRequest;
import com.ssafy.interviewstudy.dto.conference.ConferenceResponse;
import com.ssafy.interviewstudy.repository.conference.ConferenceAttendeeRepository;
import com.ssafy.interviewstudy.repository.conference.ConferenceRoomRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ConferenceService {

    private final EntityManager em;
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final ConferenceAttendeeRepository conferenceAttendeeRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    public void createConference(ConferenceRequest conferenceRequest) {


        Study study = studyRepository.findById(conferenceRequest.getStudyId()).get();
        ConferenceRoom conferenceRoom = new ConferenceRoom(study);
        conferenceRoomRepository.save(conferenceRoom);
    }

    public ConferenceResponse joinConference(ConferenceRequest conferenceRequest) {
        ConferenceRoom conferenceRoom = conferenceRoomRepository.findById(conferenceRequest.getConferenceRoomId()).get();
        Member member = memberRepository.findById(conferenceRequest.getMemberId()).get();
        ConferenceAttendee conferenceAttendee = new ConferenceAttendee(conferenceRoom, member);
        conferenceAttendeeRepository.save(conferenceAttendee);

        return new ConferenceResponse(conferenceRoom, conferenceRoom.getConferenceAttendeeList());
    }

    public void exitConference(ConferenceExitRequest conferenceExitRequest) {
        conferenceAttendeeRepository.deleteById(conferenceExitRequest.getAttendeeId());
        ConferenceRoom conferenceRoom = conferenceRoomRepository.findById(conferenceExitRequest.getConferenceRoomId()).get();
        if (conferenceRoom.getConferenceAttendeeList().size() == 0 || conferenceRoom.getConferenceAttendeeList() == null) {
            conferenceRoomRepository.deleteById(conferenceRoom.getId());
        }
    }
}

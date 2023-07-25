package com.ssafy.interviewstudy.repository.conference;

import com.ssafy.interviewstudy.domain.conference.ConferenceAttendee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceAttendeeRepository extends JpaRepository<ConferenceAttendee, Integer> {
}

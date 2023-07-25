package com.ssafy.interviewstudy.repository.conference;

import com.ssafy.interviewstudy.domain.conference.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom,Integer> {

}

package com.ssafy.interviewstudy.repository.conference;

import com.ssafy.interviewstudy.domain.conference.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConferenceRepository extends JpaRepository<ConferenceRoom,Long> {

    @Override
    Optional<ConferenceRoom> findById(Long id);

    @Override
    boolean existsById(Long id);
}

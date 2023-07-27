package com.ssafy.interviewstudy.repository.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    public SseEmitter save(String id, SseEmitter sseEmitter);

    public void deleteSseEmitterById(String id);

    public Map<String,SseEmitter> getEmittersByMemberId(Integer memberId);

}

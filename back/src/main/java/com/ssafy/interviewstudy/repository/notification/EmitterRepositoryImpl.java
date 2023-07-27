package com.ssafy.interviewstudy.repository.notification;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository{

    private static final ConcurrentHashMap<String, SseEmitter> sseEmitterConcurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String memberId,SseEmitter sseEmitter){
        String mapId = memberId+"_"+System.currentTimeMillis();
        sseEmitterConcurrentHashMap.put(mapId,sseEmitter);
        return sseEmitter;
    }

    @Override
    public void deleteSseEmitterById(String id) {
        sseEmitterConcurrentHashMap.remove(id);
    }

    @Override
    public Map<String,SseEmitter> getEmittersByMemberId(Integer memberId) {
        return sseEmitterConcurrentHashMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(memberId+"_"))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }
}

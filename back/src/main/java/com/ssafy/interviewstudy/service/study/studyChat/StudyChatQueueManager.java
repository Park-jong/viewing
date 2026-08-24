package com.ssafy.interviewstudy.service.study.studyChat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

//스터디별로 채팅 메시지를 하나의 큐(단일 스레드)에서 순서대로 처리하기 위한 매니저
@Slf4j
@Component
public class StudyChatQueueManager {

    private static final long IDLE_TIMEOUT_MILLIS = Duration.ofMinutes(10).toMillis();

    private final ConcurrentHashMap<Integer, StudyChatQueue> queues = new ConcurrentHashMap<>();

    public <T> Future<T> submit(Integer studyId, Callable<T> task) {
        AtomicReference<Future<T>> futureRef = new AtomicReference<>();
        queues.compute(studyId, (id, queue) -> {
            if (queue == null) {
                queue = new StudyChatQueue();
            }
            queue.touch();
            futureRef.set(queue.executor.submit(task));
            return queue;
        });
        return futureRef.get();
    }

    //일정 시간 이상 사용되지 않은 스터디 큐(스레드)를 정리
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void evictIdleQueues() {
        long now = System.currentTimeMillis();
        for (Integer studyId : queues.keySet()) {
            queues.computeIfPresent(studyId, (id, queue) -> {
                if (now - queue.lastUsedAt.get() < IDLE_TIMEOUT_MILLIS) {
                    return queue;
                }
                queue.executor.shutdown();
                log.info("스터디 {} 채팅 큐 정리", id);
                return null;
            });
        }
    }

    private static class StudyChatQueue {
        private final ExecutorService executor = Executors.newSingleThreadExecutor();
        private final AtomicLong lastUsedAt = new AtomicLong(System.currentTimeMillis());

        private void touch() {
            lastUsedAt.set(System.currentTimeMillis());
        }
    }
}

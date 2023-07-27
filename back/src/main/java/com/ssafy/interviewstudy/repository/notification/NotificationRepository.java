package com.ssafy.interviewstudy.repository.notification;

import com.ssafy.interviewstudy.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {

    List<Notification> findNotificationsByIdGreaterThanAndAuthorId(Integer lastEventId,Integer authorId);

    List<Notification> findNotificationsByAuthorId(Integer authorId);

}

package com.ssafy.interviewstudy.controller.advice;

import com.ssafy.interviewstudy.controller.calendar.CalendarController;
import com.ssafy.interviewstudy.controller.message.MessageController;
import com.ssafy.interviewstudy.exception.calendar.updateFailException;
import com.ssafy.interviewstudy.exception.message.CreationFailException;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {MessageController.class, CalendarController.class})
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(CreationFailException.class)
    public ResponseEntity<?> handleCreationFailException(CreationFailException e){
        return ResponseEntity.internalServerError().body(e.getCreation()+" 생성 실패");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(updateFailException.class)
    public ResponseEntity<?> handleUpdateFailException(updateFailException e){
        return ResponseEntity.internalServerError().body(e.getTarget()+" 수정 실패");
    }
}

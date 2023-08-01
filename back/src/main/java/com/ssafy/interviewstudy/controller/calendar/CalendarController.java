package com.ssafy.interviewstudy.controller.calendar;

import com.ssafy.interviewstudy.dto.calendar.CalendarCreatedResponse;
import com.ssafy.interviewstudy.dto.calendar.CalendarDto;
import com.ssafy.interviewstudy.service.calendar.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping(value = {"users/{userId}/calendars"})
@RestController
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public ResponseEntity<?> getCalendars(@PathVariable Integer userId){
        return ResponseEntity.ok().body(calendarService.getCalendarList(userId));
    }

    @PostMapping
    public ResponseEntity<?> createCalendar(@Valid @RequestBody CalendarDto calendarDto,@PathVariable Integer userId){
        calendarDto.setMemberId(userId);
        System.out.println("calendarDto : "+calendarDto);
        CalendarCreatedResponse calendarCreatedResponse = calendarService.createCalendar(calendarDto);
        return ResponseEntity
                .created(URI.create("/"+calendarCreatedResponse.getCalendarId()))
                .body(calendarCreatedResponse);
    }

    @PutMapping("/{calendarId}")
    public ResponseEntity<?> updateCalendar(@Valid @RequestBody CalendarDto calendarDto,
                                            @PathVariable(required = true) Integer calendarId,
                                            @PathVariable(required = true) Integer userId){
        calendarDto.setMemberId(userId);
        calendarDto.setCalendarId(calendarId);
        calendarService.updateCalendar(calendarDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{calendarId}")
    public ResponseEntity<?> deleteCalendar(@PathVariable(required = true) Integer calendarId){
        calendarService.deleteCalendar(calendarId);
        return ResponseEntity.ok().build();
    }

}

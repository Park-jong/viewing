package com.ssafy.interviewstudy.dto.calendar;

import com.ssafy.interviewstudy.domain.calendar.Calendar;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class CalendarListResponse {

    private List<CalendarRetrieveResponse> data = new ArrayList<>();

    public static CalendarListResponse fromEntity(List<Calendar> calendarList){

        CalendarListResponse calendarListResponse = new CalendarListResponse();
        for(Calendar c : calendarList){
            calendarListResponse.getData().add(CalendarRetrieveResponse.fromEntity(c));
        }
        return calendarListResponse;
    }
}

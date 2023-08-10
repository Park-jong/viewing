package com.ssafy.interviewstudy.interceptor.connection;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.stereotype.Component;

public class ConnectionConfig {
    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor(){
        return new OpenEntityManagerInViewInterceptor();
    }


}

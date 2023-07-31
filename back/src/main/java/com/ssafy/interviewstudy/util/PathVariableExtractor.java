package com.ssafy.interviewstudy.util;

import java.util.ArrayList;
import java.util.List;

public class PathVariableExtractor {
    public static List<Integer> extract(String uri){
        String[] subUris = uri.split("\\/");
        List<Integer> pathVariables = new ArrayList<>();
        for(String s : subUris){
            if(s.matches("\\d+")){
                pathVariables.add(Integer.parseInt(s));
            }
        }
        return pathVariables;
    }
}

package com.ssafy.interviewstudy.dto.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.member.MemberProfileBackground;
import com.ssafy.interviewstudy.domain.member.MemberProfileImage;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberProfileChangeDto {

    private Integer userId;

    private MemberProfileImage character;


    private MemberProfileBackground background;

}

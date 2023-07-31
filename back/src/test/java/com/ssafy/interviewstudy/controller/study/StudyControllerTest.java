package com.ssafy.interviewstudy.controller.study;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.dto.study.StudyDtoRequest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudyControllerTest {

    @LocalServerPort
    int port;

    String auth = "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc";

    @BeforeEach
    void init(){
        RestAssured.port = this.port;
    }

    @Test
    public void getStudyListTest(){
        ExtractableResponse<Response> results[] = new ExtractableResponse[4];
        results[0] = RestAssured
                .given()
                .when()
                .get("/studies")
                .then().log().all()
                .extract();
        results[1] = RestAssured
                .given()
                .param("page", 1)
                .when()
                .get("/studies")
                .then().log().all()
                .extract();
        results[2] = RestAssured
                .given()
                .param("page", 2)
                .when()
                .get("/studies")
                .then().log().all()
                .extract();
        results[3] = RestAssured
                .given()
                .param("page", 3)
                .when()
                .get("/studies")
                .then().log().all()
                .extract();
        for (ExtractableResponse<Response> result : results) {
            JsonPath jsonPath = result.jsonPath();
            assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(jsonPath.getInt("size")).isEqualTo(12);
            assertThat(jsonPath.getInt("numberOfElements")).isIn(0, 12, jsonPath.getInt("totalElements") % jsonPath.getInt("size"));
            System.out.println(jsonPath.getInt("numberOfElements"));
        }
    }

    @Test
    public void getStudyTest(){
        ExtractableResponse<Response> result = RestAssured
                .given()
                .header("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc")
                .when()
                .get("/studies/1")
                .then().log().all()
                .extract();
        JsonPath jsonPath = result.jsonPath();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getInt("study_id")).isEqualTo(1);

        ExtractableResponse<Response> result2 = RestAssured
                .given()
                .header("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc")
                .when()
                .get("/studies/2")
                .then().log().all()
                .extract();
        JsonPath jsonPath2 = result2.jsonPath();
        assertThat(result2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath2.getInt("study_id")).isEqualTo(2);
    }

    @Test
    public void postStudyTest(){
        StudyDtoRequest study = StudyDtoRequest.builder().title("test code").description("test 코드용").leaderId(1).appliedCompany(1).tags(new ArrayList<>()).capacity(7).careerLevel(CareerLevel.valueOf("ALL")).appliedJob("직무").recruitment(true).build();

        ExtractableResponse<Response> result = RestAssured
                .given()
                .header("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc")
                .body(study)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies")
                .then().log().all()
                .extract();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    @Test
    public void studyDeleteTest(){

        ExtractableResponse<Response> result = RestAssured
                .given()
                .header("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc")
                .when()
                .delete("/studies/1")
                .then().log().all()
                .extract();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    @Test
    public void studyPutTest(){
        StudyDtoRequest study = StudyDtoRequest.builder().title("test 수정").description("test 코드용 수정").leaderId(1).appliedCompany(1).tags(new ArrayList<>()).capacity(7).careerLevel(CareerLevel.valueOf("ALL")).appliedJob("직무").recruitment(true).build();

        ExtractableResponse<Response> result = RestAssured
                .given()
                .header("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc")
                .body(study)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/studies/1")
                .then().log().all()
                .extract();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void studyRequestPostTest(){
        String body = "{\n" +
                "    \"content\":\"안녕하세요\",\n" +
                "    \"user_id\":2,\n" +
                "    \"request_files\":[]\n" +
                "}";
        ExtractableResponse<Response> result = RestAssured
                .given()
                .body(body)
                .header("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies/1/requests")
                .then().log().all()
                .extract();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void studyRequestGet(){
        ExtractableResponse<Response> result = RestAssured
                .given()
                .header("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc")
                .when()
                .get("/studies/1/requests")
                .then().log().all()
                .extract();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void studyRequestGetDetail(){
        ExtractableResponse<Response> result = RestAssured
                .given()
                .header("Authorization", "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MTY5MDg2MzUyOCwiaWF0IjoxNjkwNzc3MTI4LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.kyA6xr5zGx2oDnqMInh8w-UyJrmrbFHw33TGiFMLbXc")
                .when()
                .get("/studies/1/requests/1")
                .then().log().all()
                .extract();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
package com.ssafy.interviewstudy.controller.study;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.dto.study.StudyDtoRequest;
import com.ssafy.interviewstudy.repository.study.StudyRequestRepository;
import com.ssafy.interviewstudy.service.study.StudyService;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

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

    String authId1 = "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MjAwMDAwMDAwMCwiaWF0IjoxNjkwODQ4MDI3LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6MX0.ZfJu9Dx4Si9ImOVtoX_tjYLiNHMuZq8NTDWb2R6D1Rw";
    String authId2 = "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MjAwMDAwMDAwMCwiaWF0IjoxNjkwODQ4MDI3LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6Mn0.yZDA8XFBExrQPOIRWfLdICpiawk1kSB6wzHKt2f-d8g";
    String authId3 = "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJJbnRlcnZpZXdTdHVkeSIsImV4cCI6MjAwMDAwMDAwMCwiaWF0IjoxNjkwODQ4MDI3LCJlbWFpbCI6InRrZHdvNzY5OUBnbWFpbC5jb20iLCJtZW1iZXJJZCI6M30.PLViJmJgeRs9baSYJ2Mw34Xjjyi1roCUyJ89x4MkNvs";

    @Autowired
    private StudyService studyService;

    @Autowired
    private StudyRequestRepository studyRequestRepository;

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
    public void postGetPutDeleteStudyTest(){
        //post
        StudyDtoRequest study = StudyDtoRequest.builder().title("test code").description("test 코드용").leaderId(1).appliedCompany(1).tags(new ArrayList<>()).capacity(7).careerLevel(CareerLevel.valueOf("ALL")).appliedJob("직무").recruitment(true).build();

        ExtractableResponse<Response> postResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .body(study)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies")
                .then().log().all()
                .extract();
        assertThat(postResult.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String id = postResult.response().getBody().print();


        //get
        ExtractableResponse<Response> getResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .get("/studies/"+id)
                .then().log().all()
                .extract();
        JsonPath jsonPath = getResult.jsonPath();
        assertThat(getResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getInt("study_id")).isEqualTo(Integer.parseInt(id));

        //put
        StudyDtoRequest updateStudy = StudyDtoRequest.builder().title("test 수정").description("test 코드용 수정").leaderId(1).appliedCompany(1).tags(new ArrayList<>()).capacity(7).careerLevel(CareerLevel.valueOf("ALL")).appliedJob("직무").recruitment(true).build();

        ExtractableResponse<Response> putResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .body(updateStudy)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/studies/"+id)
                .then().log().all()
                .extract();
        assertThat(putResult.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //get
        ExtractableResponse<Response> getResult2 = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .get("/studies/"+id)
                .then().log().all()
                .extract();
        JsonPath jsonPath2 = getResult2.jsonPath();
        assertThat(getResult2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath2.getInt("study_id")).isEqualTo(Integer.parseInt(id));
        assertThat(jsonPath2.getString("title")).isEqualTo("test 수정");

        //delete
        ExtractableResponse<Response> deleteResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .delete("/studies/"+id)
                .then().log().all()
                .extract();
        assertThat(deleteResult.statusCode()).isEqualTo(HttpStatus.OK.value());

    }


    @Test
    public void studyRequestGet(){
        ExtractableResponse<Response> result = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .get("/studies/1/requests")
                .then().log().all()
                .extract();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void studyRequestTest(){
        int studyId = 1;
        int memberId1 = 2;
        int memberId2 = 3;


        String body1 = "{\n" +
                "    \"content\":\"안녕하세요\",\n" +
                "    \"user_id\":"+memberId1+",\n" +
                "    \"request_files\":[]\n" +
                "}";

        String body2 = "{\n" +
                "    \"content\":\"안녕하세요\",\n" +
                "    \"user_id\":"+memberId2+",\n" +
                "    \"request_files\":[]\n" +
                "}";

        //신청
        ExtractableResponse<Response> postResult = RestAssured
                .given()
                .body(body1)
                .header("Authorization", authId2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies/"+studyId+"/requests")
                .then().log().all()
                .extract();
        assertThat(postResult.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String resultId = postResult.response().getBody().print();

        ExtractableResponse<Response> postResult2 = RestAssured
                .given()
                .body(body2)
                .header("Authorization", authId3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies/"+studyId+"/requests")
                .then().log().all()
                .extract();
        assertThat(postResult2.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String resultId2 = postResult2.response().getBody().print();

        //조회
        ExtractableResponse<Response> getResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .get("/studies/"+studyId+"/requests/"+resultId)
                .then().log().all()
                .extract();
        assertThat(getResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        JsonPath jsonPath = getResult.jsonPath();
        Map user = (Map)jsonPath.getJsonObject("user");
        int userId = (int)user.get("id");
        assertThat(userId).isEqualTo(memberId1);
        assertThat(jsonPath.getInt("request_id")).isEqualTo(Integer.valueOf(resultId));

        //승인
        ExtractableResponse<Response> approvalResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .body("{\"user_id\":"+ memberId1+"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies/"+studyId+"/requests/"+resultId+"/approval")
                .then().log().all()
                .extract();
        assertThat(approvalResult.statusCode()).isEqualTo(HttpStatus.OK.value());


        //거절
        ExtractableResponse<Response> denialResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .body("{\"user_id\":"+ memberId2+"}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies/"+studyId+"/requests/"+resultId2+"/denial")
                .then().log().all()
                .extract();
        assertThat(denialResult.statusCode()).isEqualTo(HttpStatus.OK.value());

        //신청 3
        ExtractableResponse<Response> postResult3 = RestAssured
                .given()
                .body(body2)
                .header("Authorization", authId3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies/"+studyId+"/requests")
                .then().log().all()
                .extract();
        assertThat(postResult3.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String resultId3 = postResult3.response().getBody().print();


        //취소
        ExtractableResponse<Response> cancelResult = RestAssured
                .given()
                .header("Authorization", authId3)
                .when()
                .delete("/studies/"+studyId+"/requests/"+resultId3)
                .then().log().all()
                .extract();
        assertThat(cancelResult.statusCode()).isEqualTo(HttpStatus.OK.value());

        //get
        ExtractableResponse<Response> studyResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .get("/studies/"+studyId)
                .then().log().all()
                .extract();
        JsonPath studyJsonPath = studyResult.jsonPath();
        assertThat(studyResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        Map leader = (Map)studyJsonPath.getJsonObject("leader");
        int leaderId = (int) leader.get("id");

        //스터디장 위임
        ExtractableResponse<Response> leaderChange = RestAssured
                .given()
                .header("Authorization", authId1)
                .body("{\n" +
                        "    \"before_leader_id\":"+leaderId+",\n" +
                        "    \"after_leader_id\":"+memberId1+"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/studies/"+studyId+"/members/leader")
                .then().log().all()
                .extract();
        assertThat(leaderChange.statusCode()).isEqualTo(HttpStatus.OK.value());

        //get
        ExtractableResponse<Response> studyResult2 = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .get("/studies/"+studyId)
                .then().log().all()
                .extract();
        JsonPath studyJsonPath2 = studyResult2.jsonPath();
        assertThat(studyResult2.statusCode()).isEqualTo(HttpStatus.OK.value());
        Map changeleader = (Map)studyJsonPath2.getJsonObject("leader");
        int changeleaderId = (int) changeleader.get("id");
        assertThat(changeleaderId).isEqualTo(memberId1);

        //스터디장 위임
        ExtractableResponse<Response> leaderChange2 = RestAssured
                .given()
                .header("Authorization", authId2)
                .body("{\n" +
                        "    \"before_leader_id\":"+memberId1+",\n" +
                        "    \"after_leader_id\":"+leaderId+"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/studies/"+studyId+"/members/leader")
                .then().log().all()
                .extract();
        assertThat(leaderChange2.statusCode()).isEqualTo(HttpStatus.OK.value());

        //추방
        ExtractableResponse<Response> banResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .delete("/studies/"+studyId+"/members/"+memberId1+"/exit")
                .then().log().all()
                .extract();
        assertThat(banResult.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void getStudyMembersTest(){
        ExtractableResponse<Response> result = RestAssured
                .given()
                .header("Authorization", authId2)
                .when()
                .get("/studies/2/members")
                .then().log().all()
                .extract();
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void studyCalendarTest(){
        int studyId = 2;
        String body = "{\n" +
                "    \"user_id\":1,\n" +
                "    \"started_at\":\"2023-08-11T21:59\",\n" +
                "    \"ended_at\":\"2023-08-11T23:59\",\n" +
                "    \"description\":\"일정 테스트\"\n" +
                "}";
        //일정 등록
        ExtractableResponse<Response> postResult = RestAssured
                .given()
                .body(body)
                .header("Authorization", authId1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/studies/"+studyId+"/calendars")
                .then().log().all()
                .extract();
        assertThat(postResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        String resultId = postResult.response().getBody().print();

        //일정 조회
        ExtractableResponse<Response> getResult = RestAssured
                .given()
                .header("Authorization", authId2)
                .when()
                .get("/studies/"+studyId+"/calendars/"+resultId)
                .then().log().all()
                .extract();
        assertThat(getResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResult.jsonPath().getString("description")).isEqualTo("일정 테스트");

        //일정 수정
        ExtractableResponse<Response> putResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .body("{\n" +
                        "    \"started_at\":\"2023-08-11T21:59\",\n" +
                        "    \"ended_at\":\"2023-08-11T23:59\",\n" +
                        "    \"description\":\"일정 수정\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/studies/"+studyId+"/calendars/"+resultId)
                .then().log().all()
                .extract();
        assertThat(putResult.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> getResult2 = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .get("/studies/"+studyId+"/calendars/"+resultId)
                .then().log().all()
                .extract();
        assertThat(getResult2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResult2.jsonPath().getString("description")).isEqualTo("일정 수정");

        //일정 삭제
        ExtractableResponse<Response> deleteResult = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .delete("/studies/"+studyId+"/calendars/"+resultId)
                .then().log().all()
                .extract();
        assertThat(deleteResult.statusCode()).isEqualTo(HttpStatus.OK.value());

        //일정 전체 조회
        ExtractableResponse<Response> getResults = RestAssured
                .given()
                .header("Authorization", authId1)
                .when()
                .get("/studies/"+studyId+"/calendars")
                .then().log().all()
                .extract();
        assertThat(getResults.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
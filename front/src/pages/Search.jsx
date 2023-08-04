import styled from "styled-components";

import StudyCard from "../components/Study/StudyCard";
import Toggle from "./../components/Common/Toggle";
import SearchBox from "../components/Home/SearchBox";
import StyledButton from "../components/Button/StyledButton";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import Tag from "../components/Study/Tag";

const data = [
  {
    content: [
      {
        study_id: 2,
        title: "test2",
        description: null,
        applied_company: "삼성",
        applied_job: "it",
        capacity: 6, //전체 정원
        head_count: 2, // 현재인원
        created_at: "2023-07-26T15:17",
        deadline: "2023-07-26T15:17", //마감날짜.
        recruitment: true,
        leader: {
          member_id: 11,
          nickname: "jiwoo",
          background: "red",
          character: "cow",
        },
        career_level: "ALL",
        tags: ["tag1", "tag2", "a", "b", "c"],
      },
      {
        study_id: 3,
        title: "test2",
        description: null,
        applied_company: "삼성",
        applied_job: "it",
        capacity: 0, //전체 정원
        head_count: 2, // 현재인원
        created_at: "2023-07-26T15:17",
        deadline: "2023-07-26T15:17", //마감날짜.
        recruitment: true,
        leader: {
          member_id: 22,
          nickname: "jiwoo",
          background: "red",
          character: "cow",
        },
        career_level: "ALL",
        tags: ["tag1", "tag2"],
      },
      {
        study_id: 4,
        title: "test2",
        description: null,
        applied_company: "삼성",
        applied_job: "it",
        capacity: 0, //전체 정원
        head_count: 2, // 현재인원
        created_at: "2023-07-26T15:17",
        deadline: "2023-07-26T15:17", //마감날짜.
        recruitment: true,
        leader: {
          member_id: 33,
          nickname: "jiwoo",
          background: "red",
          character: "cow",
        },
        career_level: "ALL",
        tags: ["tag1", "tag2"],
      },
    ],
    pageable: {
      sort: {
        sorted: false,
        empty: true,
        unsorted: true,
      },
      pageSize: 20,
      pageNumber: 0,
      offset: 0,
      paged: true,
      unpaged: false,
    },
    totalPages: 1, //20개씩 있는 페이지의 수
    totalElements: 5, //전체 스터디 개수
    last: true, //마지막페이지인지
    number: 0, //현재페이지 넘버
    size: 20, // 한페이지에 20개씩
    numberOfElements: 5, //현재 페이지에 있는 스터디의 수
    sort: {
      sorted: false,
      empty: true,
      unsorted: true,
    },
    first: true, //첫페이지인지
    empty: false, //검색결과가 없는지
  },
];

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const ToggleContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
  margin-bottom: 8px;
`;

const SearchContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
`;

// Styled div for the horizontal line
const HorizontalLine = styled.div`
  width: 100%;
  height: 1px;
  background-color: var(--gray-200); /* You can change the color as needed */
  margin-bottom: 20px;
`;

const TagContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

const BodyContainer = styled.div`
  display: flex;
  justify-content: flex-start;
`;

///studies?appliedCompany={지원회사(string)}&job={직무(String)}&careerLevel={신입/경력/무관(ALL, INTERN, NEWCOMER, EXPERIENCED)}&option=true&page=0&size=20&sort=id,desc&sort=username,desc

export default function Search() {
  const url = new URL(document.URL);
  const query = url.searchParams; //?appliedCompany=%E3%85%87%E3%85%87&job=hh&careerLevel=ALL
  console.log(query.get("appliedCompany"));

  const appliedCompany = query.get("appliedCompany");
  const job = query.get("job");
  const careerLevel = query.get("careerLevel");

  const [searchData, SetsearchData] = useState([]);

  console.log(searchData);

  //search 통신 보내기
  // useEffect(() => {
  //   customAxios()
  //     .get(
  //       `studies?appliedCompany=${appliedCompany}&job=${job}&careerLevel=${careerLevel}`
  //     )
  //     .then((res) => {
  //       SetsearchData(res.data);
  //     });
  // }, []);

  const handleClick = () => {};

  return (
    <Container>
      <ToggleContainer>
        <Toggle />
      </ToggleContainer>
      <SearchContainer>
        {/* <MainButton
          marginright={10}
          width={200}
          height={45}
          fontSize={16}
          content="스터디 생성하기
"
          onClick={handleClick}
        /> */}

        <StyledButton
          marginright={10}
          width={200}
          height={45}
          fontSize={16}
          content="스터디 생성하기"
          onClick={handleClick}
        />
        <SearchBox width={950} />
      </SearchContainer>

      <HorizontalLine></HorizontalLine>

      <TagContainer>
        <Tag />
      </TagContainer>

      <BodyContainer>
        {data[0].content && data[0].content.length > 0 ? (
          data[0].content.map((study, idx) => (
            <StudyCard key={idx} study={study} />
          ))
        ) : (
          <p>찾으시는 스터디가 없습니다.</p>
        )}
      </BodyContainer>
    </Container>
  );
}

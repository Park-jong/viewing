import styled from "styled-components";

import StudyCard from "../components/Study/StudyCard";
import Toggle from "./../components/Common/Toggle";
import SearchBox from "../components/Home/SearchBox";
import StyledButton from "../components/Button/StyledButton";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import TagStyled from "../components/Study/TagStyled";
import TagStyledSelected from "../components/Study/TagStyledSelected";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import {
  BiChevronLeft,
  BiChevronRight,
  BiChevronsLeft,
  BiChevronsRight,
} from "react-icons/bi";

const tags = [
  "자소서 제출 필수",
  "합격인증 필수",
  "압박면접",
  "정보공유",
  "영상회의 필수",
  "중고신입",
  "온라인 진행",
  "오프라인 진행",
  "온오프라인 혼합",
  "피드백 필수",
  "초보 환영",
];

const page = [-2, -1, 0, 1, 2];

// const data = [
//   {
//     content: [
//       {
//         study_id: 2,
//         title: "test2",
//         description: null,
//         applied_company: "삼성",
//         applied_job: "it",
//         capacity: 6, //전체 정원
//         head_count: 2, // 현재인원
//         created_at: "2023-07-26T15:17",
//         deadline: "2023-07-26T15:17", //마감날짜.
//         recruitment: true,
//         leader: {
//           member_id: 11,
//           nickname: "jiwoo",
//           background: "red",
//           character: "cow",
//         },
//         career_level: "ALL",
//         tags: ["tag1", "tag2", "a", "b", "c"],
//       },
//       {
//         study_id: 3,
//         title: "test2",
//         description: null,
//         applied_company: "삼성",
//         applied_job: "it",
//         capacity: 0, //전체 정원
//         head_count: 2, // 현재인원
//         created_at: "2023-07-26T15:17",
//         deadline: "2023-07-26T15:17", //마감날짜.
//         recruitment: true,
//         leader: {
//           member_id: 22,
//           nickname: "jiwoo",
//           background: "red",
//           character: "cow",
//         },
//         career_level: "ALL",
//         tags: ["tag1", "tag2"],
//       },
//       {
//         study_id: 4,
//         title: "test2",
//         description: null,
//         applied_company: "삼성",
//         applied_job: "it",
//         capacity: 0, //전체 정원
//         head_count: 2, // 현재인원
//         created_at: "2023-07-26T15:17",
//         deadline: "2023-07-26T15:17", //마감날짜.
//         recruitment: false,
//         leader: {
//           member_id: 33,
//           nickname: "jiwoo",
//           background: "red",
//           character: "cow",
//         },
//         career_level: "ALL",
//         tags: ["tag1", "tag2"],
//       },
//     ],
//     pageable: {
//       sort: {
//         sorted: false,
//         empty: true,
//         unsorted: true,
//       },
//       pageSize: 20,
//       pageNumber: 0,
//       offset: 0,
//       paged: true,
//       unpaged: false,
//     },
//     totalPages: 1, //20개씩 있는 페이지의 수
//     totalElements: 5, //전체 스터디 개수
//     last: true, //마지막페이지인지
//     number: 0, //현재페이지 넘버
//     size: 20, // 한페이지에 20개씩
//     numberOfElements: 5, //현재 페이지에 있는 스터디의 수
//     sort: {
//       sorted: false,
//       empty: true,
//       unsorted: true,
//     },
//     first: true, //첫페이지인지
//     empty: false, //검색결과가 없는지
//   },
// ];

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

const HorizontalLine = styled.div`
  width: 100%;
  height: 1px;
  background-color: var(--gray-200);
  margin-bottom: 20px;
`;

const TagContainer = styled.div`
  display: flex;
  justify-content: space-between;
  overflow-x: auto;

  padding-bottom: 20px;
`;

const TagRadio = styled.input``;

const BodyContainer = styled.div`
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
`;

const TagLabel = styled.label`
  user-select: none;
  cursor: pointer;
`;

const PageArea = styled.div`
  text-align: center;
  display: inline-flex;
  justify-content: center;
  align-items: center;
`;

const PageNum = styled.div`
  margin: 5px;
  border-radius: 10px;
  width: 16px;
  height: 16px;
  cursor: pointer;
  &:hover {
    background-color: var(--gray-200);
  }
`;

const CurrentNum = styled.div`
  margin: 5px;
  border-radius: 10px;
  width: 16px;
  height: 16px;
  background-color: var(--gray-200);
`;

const ArrowPage = styled.div`
  margin: 5px;
  border-radius: 10px;
  width: 16px;
  height: 16px;
  cursor: pointer;
  &:hover {
    background-color: var(--gray-200);
  }
`;

const NoneDiv = styled.div`
  margin: 5px;
  border-radius: 10px;
  width: 16px;
  height: 16px;
`;

///studies?appliedCompany={지원회사(string)}&job={직무(String)}&careerLevel={신입/경력/무관(ALL, INTERN, NEWCOMER, EXPERIENCED)}&option=true&page=0&size=20&sort=id,desc&sort=username,desc

export default function Search() {
  const url = new URL(document.URL);
  const query = url.searchParams; //?appliedCompany=%E3%85%87%E3%85%87&job=hh&careerLevel=ALL
  // console.log(query.get("appliedCompany"));

  const appliedCompany = query.get("appliedCompany");
  const job = query.get("job");
  const careerLevel = query.get("careerLevel");

  const [searchPage, setSearchPage] = useState(0);
  const [pageSize, setPageSize] = useState(0);

  const [searchData, setSearchData] = useState({ content: [] });
  const [searchTag, setSearchTag] = useState(null);

  //토글 로직
  const [isToggled, setIsToggled] = useState(false);
  const [filteredData, setFilteredData] = useState([]);

  const member = useSelector((state) => state.UserReducer);
  const navigate = useNavigate();

  const getSearchUrl = () =>
    `studies?${appliedCompany ? "appliedCompany=" + appliedCompany : ""}&${
      job ? "appliedJob=" + job : ""
    }&${careerLevel ? "careerLevel=" + careerLevel : ""}&option=${isToggled}&${
      searchTag ? "tag=" + searchTag : ""
    }`;

  const newDataSearch = () => {
    customAxios()
      .get(getSearchUrl())
      .then((res) => {
        setSearchData(() => res.data);
        setSearchPage(() => res.data.number);
        setPageSize(() => res.data.totalPages);
      });
  };

  const pageMove = (n) => {
    customAxios()
      .get(getSearchUrl() + `&page=${n}`)
      .then((res) => {
        setSearchData(() => res.data);
        setSearchPage(() => res.data.number);
        setPageSize(() => res.data.totalPages);
      });
  };

  //search 통신 보내기
  useEffect(() => {
    newDataSearch();
  }, []);

  const handleClick = () => {
    if (member.memberId) navigate("/makestudy");
    else navigate("/login");
  };

  useEffect(() => {
    newDataSearch();
  }, [isToggled]);

  useEffect(() => {
    newDataSearch();
  }, [searchTag]);

  useEffect(() => {
    setFilteredData(searchData.content);
  }, [searchData]);

  // const handleToggle = () => {
  //   setIsToggled(!isToggled);
  // };

  return (
    <Container>
      <ToggleContainer>
        <Toggle
          onClick={() => setIsToggled(!isToggled)}
          isToggled={isToggled}
          setIsToggled={setIsToggled}
        />
      </ToggleContainer>
      <SearchContainer>
        <StyledButton
          marginright={10}
          width={200}
          height={45}
          fontSize={16}
          content="스터디 생성하기"
          onClick={() => {
            handleClick();
          }}
        />
        <SearchBox
          width={950}
          appliedCompany={appliedCompany}
          job={job}
          careerLevel={careerLevel}
        />
      </SearchContainer>

      <HorizontalLine></HorizontalLine>

      <TagContainer>
        {tags.map((tag, idx) => (
          <div key={idx}>
            <TagRadio
              type="radio"
              value={idx + 1}
              id={tag}
              name="tag"
              onClick={(e) => {
                if (searchTag === e.target.value) {
                  setSearchTag(() => null);
                } else {
                  setSearchTag(() => e.target.value);
                }
              }}
              hidden
            ></TagRadio>
            <TagLabel htmlFor={tag}>
              {tag === tags[searchTag - 1] ? (
                <TagStyledSelected key={idx} content={tag} />
              ) : (
                <TagStyled key={idx} content={tag} />
              )}
            </TagLabel>
          </div>
        ))}
      </TagContainer>

      <HorizontalLine></HorizontalLine>

      <BodyContainer>
        {filteredData.length > 0 ? (
          filteredData.map((study, idx) => (
            <StudyCard key={idx} study={study} />
          ))
        ) : (
          <p>찾으시는 스터디가 없습니다.</p>
        )}
      </BodyContainer>
      <PageArea>
        {searchPage - 3 >= 0 ? (
          <>
            <ArrowPage
              onClick={() => {
                pageMove(0);
              }}
            >
              <BiChevronsLeft />
            </ArrowPage>
            <ArrowPage
              onClick={() => {
                pageMove(searchPage - 3);
              }}
            >
              <BiChevronLeft />
            </ArrowPage>
          </>
        ) : (
          <>
            <NoneDiv />
            <NoneDiv />
          </>
        )}
        {page.map((num, idx) => {
          const n = num + searchPage + 1;
          return n > 0 && n <= pageSize ? (
            num === 0 ? (
              <CurrentNum key={idx}>{n}</CurrentNum>
            ) : (
              <PageNum
                key={idx}
                onClick={() => {
                  pageMove(n - 1);
                }}
              >
                {n}
              </PageNum>
            )
          ) : (
            <NoneDiv key={idx} />
          );
        })}
        {searchPage + 3 < pageSize ? (
          <>
            <ArrowPage
              onClick={() => {
                pageMove(searchPage + 3);
              }}
            >
              <BiChevronRight />
            </ArrowPage>
            <ArrowPage
              onClick={() => {
                pageMove(pageSize - 1);
              }}
            >
              <BiChevronsRight />
            </ArrowPage>
          </>
        ) : (
          <>
            <NoneDiv />
            <NoneDiv />
          </>
        )}
      </PageArea>
    </Container>
  );
}

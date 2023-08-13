import { styled } from "styled-components";
import { useLocation, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import UserProfile from "./../components/Common/UserProfile";
import { BiCrown } from "react-icons/bi";

const Container = styled.div`
  width: 500px;
`;

const Title = styled.div`
  font-size: 30px;
`;

const MemberArea = styled.div``;

const Profile = styled.span`
  display: inline-flex;
`;

const MemberName = styled.span`
  display: inline-flex;
  align-items: center;
  width: 200px;
`;

const MemberBox = styled.div`
  display: flex;
  align-items: center;
`;

const StudyName = styled.div``;

const TagArea = styled.div`
  padding-bottom: 5px;
`;

const TagBox = styled.span``;

const SelectedTagBox = styled.span`
  background: blue;
  display: inline-flex;
  align-items: center;
  margin: 5px;
  user-select: none;
`;

const UnselectedTagBox = styled.span`
  background: gray;
  display: inline-flex;
  align-items: center;
  margin: 5px;
  user-select: none;
`;

const Description = styled.div``;

const Category = styled.div`
  font-size: 20px;
`;

const Company = styled.div``;

const Job = styled.div``;

const DeadLine = styled.div``;

const DeadLineInputBox = styled.input``;

const Capacity = styled.div``;

const CareerLevel = styled.div``;

export default function StudyPkInfo() {
  const location = useLocation();

  const studyId = useParams().studyPk;

  const [memberList, setMemberList] = useState([]);
  const [tagList, setTagList] = useState([]);
  const [studyTitle, setStudyTitle] = useState("");
  const [studyDesc, setStudyDesc] = useState("");
  const [studyLeaderId, setStudyLeaderId] = useState();
  const [company, setCompany] = useState(null);
  const [job, setJob] = useState("");
  const [capacity, setCapacity] = useState(0);
  const [deadline, setDeadline] = useState(0);
  const [careerLevel, setCareerLevel] = useState("ALL");
  const [tagSelected, setTagSelected] = useState([
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
    false,
  ]);
  const loginMember = useSelector((state) => state.UserReducer);
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
  const careerType = {
    ALL: "경력무관",
    INTERN: "인턴",
    NEWCOMER: "신입",
    EXPERIENCED: "경력",
  };

  useEffect(() => {
    customAxios()
      .get(`studies/${studyId}/detail`)
      .then(({ data }) => {
        setMemberList(() => [...data.members]);
        setTagList(() => [...data.tags]);
        setStudyTitle(() => data.title);
        setStudyDesc(() => data.description);
        setStudyLeaderId(() => data.leader.member_id);
        setCompany(() => data.applied_company);
        setJob(() => data.applied_job);
        setCapacity(() => data.capacity);
        setCareerLevel(() => data.career_level);
        setDeadline(() => data.deadline.split(" ")[0]);
      });
  }, []);

  useEffect(() => {
    for (let i = 0; i < tags.length; i++) {
      if (tagList.includes(tags[i])) {
        setTagSelected((prev) => {
          const arr = [...prev];
          arr[i] = true;
          return arr;
        });
      }
    }
  }, [tagList]);

  const MemberListDoms = memberList.map((member, idx) => {
    return (
      <MemberBox key={member.member_id}>
        <Profile>
          <UserProfile
            backgroundcolor={member.background}
            characterimg={member.character}
          ></UserProfile>
        </Profile>
        <MemberName>{member.nickname}</MemberName>
        {member.member_id === studyLeaderId ? <BiCrown /> : <></>}
      </MemberBox>
    );
  });

  const TagListDoms = tags.map((tag, idx) => {
    return (
      <TagBox key={idx}>
        {tagSelected[idx] ? (
          <SelectedTagBox>{tag}</SelectedTagBox>
        ) : (
          <UnselectedTagBox>{tag}</UnselectedTagBox>
        )}
      </TagBox>
    );
  });

  return (
    <Container>
      <Title>스터디 정보</Title>
      <MemberArea>
        <Category>회원 목록</Category>
        {MemberListDoms}
      </MemberArea>
      <Capacity>
        <Category>최대 인원</Category>
        {capacity}
      </Capacity>
      <StudyName>
        <Category>스터디 제목</Category>
        {studyTitle}
      </StudyName>
      <Company>
        <Category>지원 회사</Category>
        {company}
      </Company>
      <Job>
        <Category>지원 직무</Category>
        {job}
      </Job>
      <TagArea>
        <Category>태그 목록</Category>
        {TagListDoms}
      </TagArea>
      <Description>
        <Category>스터디 설명</Category>
        {studyDesc}
      </Description>
      <CareerLevel>
        <Category>경력</Category>
        {careerType[careerLevel]}
      </CareerLevel>
      <DeadLine>
        <Category>마감일</Category>
        <DeadLineInputBox
          value={deadline}
          type="date"
          readOnly
        ></DeadLineInputBox>
      </DeadLine>
    </Container>
  );
}

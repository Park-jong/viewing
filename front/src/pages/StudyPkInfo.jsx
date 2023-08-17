import { styled } from "styled-components";
import { useLocation, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import UserProfile from "./../components/Common/UserProfile";
import { BiCrown } from "react-icons/bi";
import { useNavigate } from "react-router-dom";

const Container = styled.div`
  width: 500px;
`;

const FlexContainer = styled.div`
  display: flex;
  /* justify-content: center; */
  align-items: center;
`;

const FlexContainer2 = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;
const Title = styled.div`
  font-size: 30px;
  font-weight: 700;

  margin-bottom: 20px;
`;

const HorizontalLine = styled.div`
  width: 100%;
  height: 3px;
  background-color: var(--gray-100);
  margin-bottom: 20px;
`;

const HorizontalLine2 = styled.div`
  width: 100%;
  height: 1px;
  background-color: var(--gray-100);
  margin-bottom: 10px;
  margin-top: 20px;
`;

const MemberArea = styled.div``;

const Profile = styled.span`
  display: inline-flex;
  width: 300px;
`;

const MemberBox = styled.div`
  display: flex;
  align-items: center;
  margin-top: 25px;
`;

const StudyName = styled.div``;

const TagArea = styled.div`
  padding-bottom: 5px;
`;

const TagBox = styled.span``;

const SelectedTagBox = styled.span`
  background: #7952e2;
  display: inline-flex;
  align-items: center;
  margin: 5px;
  padding: 8px;
  border-radius: 15px;
  user-select: none;
  color: white;
  font-weight: 300;
`;

const UnselectedTagBox = styled.span`
  background: #d7d7d7;
  display: inline-flex;
  align-items: center;
  margin: 5px;
  padding: 8px;
  border-radius: 15px;
  user-select: none;
  font-weight: 300;
`;

const StudyTitle = styled.div`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  color: #000000;
  font-size: 0.9rem;
  width: 700px;
  background-color: transparent;
  box-sizing: border-box;
  padding-inline: 0.5em;
  padding-block: 0.7em;
  border: none;
  border-bottom: var(--border-height) solid var(--border-before-color);
  padding-right: 28px;
`;

const StudyDescription = styled.div`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  width: 500px;
  color: #000000;
  font-size: 0.9rem;
  background-color: transparent;
  box-sizing: border-box;
  padding: 8px;
  border: none;
  border: var(--border-height) solid var(--border-before-color);
  height: 300px;

  font-family: Pretendard;
`;

const Description = styled.div``;

const Category = styled.div`
  font-size: 20px;
  font-weight: 700;
  color: var(--gray-700);
  margin-top: 30px;
  margin-bottom: 8px;
`;

const Company = styled.div``;

const Job = styled.div``;

const DeadLine = styled.div`
  margin-left: 150px;
`;

const DeadLineInputBox = styled.input``;

const Capacity = styled.div``;

const CareerLevel = styled.div``;

export default function StudyPkInfo() {
  const location = useLocation();
  const navigate = useNavigate();

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
      })
      .catch(() => {
        alert("오류가 발생했습니다.");
        navigate("/");
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
            member_id={member.member_id}
            nickname={member.nickname}
          ></UserProfile>
        </Profile>
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
      <Title>🧾스터디 정보</Title>
      <HorizontalLine></HorizontalLine>

      <MemberArea>
        <Category>🙋회원 목록</Category>
        {MemberListDoms}
      </MemberArea>

      <FlexContainer2>
        <Company>
          <Category>지원 회사</Category>
          {company}
        </Company>
        <Job>
          <Category>지원 직무</Category>
          {job ? job : <div>없음</div>}
        </Job>
        <CareerLevel>
          <Category>경력</Category>
          {careerType[careerLevel]}
        </CareerLevel>
      </FlexContainer2>

      <FlexContainer>
        <Capacity>
          <Category>최대 인원</Category>
          {capacity}
        </Capacity>
        <DeadLine>
          <Category>마감일</Category>
          <DeadLineInputBox
            value={deadline}
            type="date"
            readOnly
          ></DeadLineInputBox>
        </DeadLine>
      </FlexContainer>

      <HorizontalLine2></HorizontalLine2>
      <StudyName>
        <Category>스터디 제목</Category>
        <StudyTitle>{studyTitle}</StudyTitle>
      </StudyName>
      <Description>
        <Category>스터디 설명</Category>
        <StudyDescription>{studyDesc}</StudyDescription>
      </Description>
      <TagArea>
        <Category>태그 목록</Category>
        {TagListDoms}
      </TagArea>
      <HorizontalLine2></HorizontalLine2>
    </Container>
  );
}

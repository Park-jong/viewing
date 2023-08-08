import { styled } from "styled-components";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import UserProfile from "./../components/Common/UserProfile";
import { BiCrown, BiSolidXSquare } from "react-icons/bi";
import StyledButton from "./../components/Button/StyledButton";
import StudyToggle from "../components/Study/StudyToggle";
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

const TagBox = styled.span`
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

const BanButton = styled.button``;

const DelegateButton = styled.button``;

const StudyTitleInputBox = styled.input``;

const StudyDescInputBox = styled.input``;

const Company = styled.div``;

const Job = styled.div``;

const StudyManage = styled.div``;

const DeleteButton = styled.button``;

const UpdateButton = styled.button``;

const DeadLine = styled.div``;

const DeadLineInputBox = styled.input``;

const Capacity = styled.div``;

const CapacityInputBox = styled.input``;

const CareerLevel = styled.div``;

const CareerLevelInputBox = styled.select``;

export default function StudyPkAdmin() {
  const location = useLocation();

  const studyId = location.pathname.split("/")[2];

  const [memberList, setMemberList] = useState([]);
  const [tagList, setTagList] = useState([]);
  const [studyTitle, setStudyTitle] = useState("");
  const [studyDesc, setStudyDesc] = useState("");
  const [studyLeaderId, setStudyLeaderId] = useState();
  const [company, setCompany] = useState(null);
  const [job, setJob] = useState(null);
  const [recruit, setRecruit] = useState(false);
  const [capacity, setCapacity] = useState(0);
  const [deadline, setDeadline] = useState(0);
  const [careerLevel, setCareerLevel] = useState("ALL");

  const [study, setStudy] = useState({
    title: null,
    description: null,
    applied_company: 1, //수정 해야함
    applied_job: null,
    capacity: 0,
    recruitment: null,
    deadline: null,
    leader_id: null,
    career_level: null,
    tags: [],
  });

  const changeTitle = (e) => {
    setStudyTitle(() => e.target.value);
  };

  const changeDesc = (e) => {
    setStudyDesc(() => e.target.value);
  };

  const changeCapacity = (e) => {
    setCapacity(() => e.target.value);
  };

  const changeDeadline = (e) => {
    setDeadline(() => e.target.value);
  };

  const changeCareerLevel = (e) => {
    setCareerLevel(() => e.target.value);
  };

  const deleteStudy = () => {
    customAxios().delete(`studies/${studyId}`);
  };

  const updateStudy = () => {
    const body = {
      title: studyTitle,
      description: studyDesc,
      applied_company: 1, //수정 해야함
      applied_job: job,
      capacity: capacity,
      recruitment: recruit,
      deadline: new Date(deadline).toJSON().split(".")[0],
      leader_id: study.leader.member_id,
      career_level: careerLevel,
      tags: [1, 2, 3],
    };
    customAxios()
      .put(`studies/${studyId}`, body)
      .then((res) => {});
  };

  const changeLeader = () => {};

  const banLeader = () => {};

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
        setRecruit(() => data.recruitment);
        setStudy(() => data);
        setCapacity(() => data.capacity);
        setCareerLevel(() => data.career_level);
        setDeadline(() => data.deadline.split("T")[0]);
      });
  }, []);

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
        {member.member_id === studyLeaderId ? (
          <BiCrown />
        ) : (
          <>
            <DelegateButton onClick={changeLeader}>
              스터디장 위임
            </DelegateButton>
            <BanButton onClick={banLeader}>추방하기</BanButton>
          </>
        )}
      </MemberBox>
    );
  });

  const TagListDoms = tagList.map((tag, idx) => {
    return <TagBox key={idx}>{tag}</TagBox>;
  });

  const CareerLevelType = () => {
    return (
      <>
        <option value={"ALL"}>경력무관</option>
        <option value={"INTERN"}>인턴</option>
        <option value={"NEWCOMER"}>신입</option>
        <option value={"EXPERIENCED"}>경력</option>
      </>
    );
  };

  return (
    <Container>
      <Title>스터디 관리</Title>
      <StudyManage>
        <StudyToggle state={recruit} setState={setRecruit}></StudyToggle>
        <DeleteButton onClick={deleteStudy}>스터디 삭제</DeleteButton>
        <UpdateButton onClick={updateStudy}>변경사항 저장</UpdateButton>
      </StudyManage>
      <MemberArea>
        <Category>회원 목록</Category>
        {MemberListDoms}
      </MemberArea>
      <Capacity>
        <Category>최대 인원</Category>
        <CapacityInputBox
          type="number"
          value={capacity}
          max={6}
          onChange={changeCapacity}
        ></CapacityInputBox>
      </Capacity>
      <StudyName>
        <Category>스터디 제목</Category>
        <StudyTitleInputBox
          type="text"
          value={studyTitle}
          onChange={changeTitle}
        ></StudyTitleInputBox>
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
        <StudyDescInputBox
          type="text"
          value={studyDesc}
          onChange={changeDesc}
        ></StudyDescInputBox>
      </Description>
      <CareerLevel>
        <Category>경력</Category>
        <CareerLevelInputBox value={careerLevel} onChange={changeCareerLevel}>
          <CareerLevelType></CareerLevelType>
        </CareerLevelInputBox>
      </CareerLevel>
      <DeadLine>
        <Category>마감일</Category>
        <DeadLineInputBox
          value={deadline}
          type="date"
          onChange={changeDeadline}
        ></DeadLineInputBox>
      </DeadLine>
    </Container>
  );
}

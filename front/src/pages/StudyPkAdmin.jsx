import { styled } from "styled-components";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import UserProfile from "./../components/Common/UserProfile";
import { BiCrown } from "react-icons/bi";
import StudyToggle from "../components/Study/StudyToggle";
import { useNavigate } from "react-router-dom";

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

const StudyJobInputBox = styled.input``;

export default function StudyPkAdmin() {
  const location = useLocation();

  const navigate = useNavigate();

  const studyId = location.pathname.split("/")[2];

  const date = new Date();
  const minDate = date.toISOString().split("T")[0];
  const [memberList, setMemberList] = useState([]);
  const [tagList, setTagList] = useState([]);
  const [studyTitle, setStudyTitle] = useState("");
  const [studyDesc, setStudyDesc] = useState("");
  const [studyLeaderId, setStudyLeaderId] = useState();
  const [company, setCompany] = useState(null);
  const [job, setJob] = useState("");
  const [recruit, setRecruit] = useState(false);
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

  const [study, setStudy] = useState({
    title: null,
    description: null,
    applied_company: "",
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

  const changeJob = (e) => {
    setJob(() => e.target.value);
  };

  const changeTagState = (idx) => {
    setTagSelected((prev) => {
      const arr = [...prev];
      arr[idx] = !arr[idx];
      return arr;
    });
  };

  const deleteStudy = () => {
    if (
      window.confirm(
        "현재 사용중인 스터디를 해체하시겠습니까?\n스터디의 데이터는 모두 삭제되며 복구할 수 없습니다."
      )
    ) {
      customAxios()
        .delete(`studies/${studyId}`)
        .then((res) => {
          alert("승인되었습니다.");
          navigate("/");
        })
        .catch(() => {
          alert("실패하였습니다.");
        });
    }
  };

  const updateStudy = () => {
    const body = {
      title: studyTitle,
      description: studyDesc,
      applied_job: job,
      capacity: capacity,
      recruitment: recruit,
      deadline: new Date(deadline).toJSON().split(".")[0],
      leader_id: study.leader.member_id,
      career_level: careerLevel,
      tags: [],
    };
    for (let i = 0; i < tagSelected.length; i++) {
      if (tagSelected[i]) {
        body.tags.push(i + 1);
      }
    }
    if (
      window.confirm(
        "스터디정보를 변경하시겠습니까?\n기존에 저장된 내용은 삭제되며 복구할 수 없습니다."
      )
    ) {
      customAxios()
        .put(`studies/${studyId}`, body)
        .then((res) => {
          alert("변경되었습니다.");
        })
        .catch(() => {
          alert("변경에 실패하였습니다.");
        });
    }
  };

  const changeLeader = (newLeaderId) => {
    if (window.confirm("스터디장 권한을 위임하시겠습니까?")) {
      customAxios()
        .put(`studies/${studyId}/members/leader`, {
          before_leader_id: loginMember.memberId,
          after_leader_id: newLeaderId,
        })
        .then((res) => {
          alert("승인되었습니다.");
          window.location.reload();
        })
        .catch(() => {
          alert("실패하였습니다.");
        });
    }
  };

  const banMember = (banMemberId) => {
    if (
      window.confirm(
        "해당 멤버를 추방하시겠습니까?\n추방된 회원의 정보는 삭제됩니다."
      )
    ) {
      customAxios()
        .delete(`studies/${studyId}/members/${banMemberId}/ban`)
        .then((res) => {
          alert("승인되었습니다.");
          setMemberList((prev) => {
            const arr = [...prev];
            for (let i = 0; i < arr.length; i++) {
              if (arr[i].member_id === banMemberId) {
                arr.splice(i, 1);
                break;
              }
            }
            return arr;
          });
        })
        .catch(() => {
          alert("실패하였습니다.");
        });
    }
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
        setRecruit(() => data.recruitment);
        setStudy(() => data);
        setCapacity(() => data.capacity);
        setCareerLevel(() => data.career_level);
        setDeadline(() => data.deadline.split("T")[0]);
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
        {member.member_id === studyLeaderId ? (
          <BiCrown />
        ) : (
          <>
            <DelegateButton
              onClick={() => {
                changeLeader(member.member_id);
              }}
            >
              스터디장 위임
            </DelegateButton>
            <BanButton onClick={() => banMember(member.member_id)}>
              추방하기
            </BanButton>
          </>
        )}
      </MemberBox>
    );
  });

  const TagListDoms = tags.map((tag, idx) => {
    return (
      <TagBox key={idx} onClick={() => changeTagState(idx)}>
        {tagSelected[idx] ? (
          <SelectedTagBox>{tag}</SelectedTagBox>
        ) : (
          <UnselectedTagBox>{tag}</UnselectedTagBox>
        )}
      </TagBox>
    );
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
        <DeleteButton onClick={() => deleteStudy()}>스터디 삭제</DeleteButton>
        <UpdateButton onClick={() => updateStudy()}>변경사항 저장</UpdateButton>
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
          min={memberList.length}
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
        <StudyJobInputBox
          type="text"
          value={job}
          onChange={changeJob}
        ></StudyJobInputBox>
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
          min={minDate}
        ></DeadLineInputBox>
      </DeadLine>
    </Container>
  );
}

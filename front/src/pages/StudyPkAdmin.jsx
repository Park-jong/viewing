import { styled } from "styled-components";
import { useLocation, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import UserProfile from "./../components/Common/UserProfile";
import { BiCrown } from "react-icons/bi";
import StudyToggle from "../components/Study/StudyToggle";
import { useNavigate } from "react-router-dom";
import MainButton from "../components/Button/MainButton";

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

const Description = styled.div``;

const Category = styled.div`
  font-size: 20px;
  font-weight: 700;
  color: var(--gray-700);
  margin-top: 30px;
  margin-bottom: 8px;
`;

const BanButton = styled.button`
  background-color: var(--secondary);
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  /* transition: background-color 0.3s, color 0.3s; */

  &:hover {
    background-color: var(--primary);
  }
`;

const DelegateButton = styled.button`
  background-color: var(--secondary);
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;

  &:hover {
    background-color: var(--primary);
  }

  margin-right: 12px;
`;

const StudyTitleInputBox = styled.input`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  width: 200px;
  color: #000000;
  font-size: 0.9rem;
  background-color: transparent;
  box-sizing: border-box;
  padding-inline: 0.5em;
  padding-block: 0.7em;
  border: none;
  border-bottom: var(--border-height) solid var(--border-before-color);
  padding-right: 28px;

  &:hover {
    background: var(--input-hovered-color);
  }

  &:focus {
    outline: none;
    border-bottom: var(--border-height) solid var(--border-after-color);
  }
`;

const StudyDescInputBox = styled.input``;

const Company = styled.div``;

const Job = styled.div``;

const StudyManage = styled.div``;

const DeleteButton = styled.button``;

const UpdateButton = styled.button``;

const DeadLine = styled.div`
  margin-left: 35px;
`;

const DeadLineInputBox = styled.input``;

const Capacity = styled.div``;

const CapacityInputBox = styled.input``;

const CareerLevel = styled.div``;

const CareerLevelInputBox = styled.select``;

const StudyJobInputBox = styled.input`
  --border-height: 1px;
  --border-before-color: rgba(221, 221, 221, 0.39);
  --border-after-color: #5891ff;
  --input-hovered-color: #4985e01f;
  position: relative;
  width: 200px;
  color: #000000;
  font-size: 0.9rem;
  background-color: transparent;
  box-sizing: border-box;
  padding-inline: 0.5em;
  padding-block: 0.7em;
  border: none;
  border-bottom: var(--border-height) solid var(--border-before-color);
  padding-right: 28px;

  &:hover {
    background: var(--input-hovered-color);
  }

  &:focus {
    outline: none;
    border-bottom: var(--border-height) solid var(--border-after-color);
  }
`;

const TextArea = styled.textarea`
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

  &:focus {
    outline: none;

    border: var(--border-height) solid var(--border-after-color);
  }
`;

// const FlexContainer = styled.div``;

export default function StudyPkAdmin() {
  const location = useLocation();

  const navigate = useNavigate();

  const studyId = useParams().studyPk;

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
    let value = e.target.value;
    if (value > 6) value = 6;
    else if (value < memberList.length) value = memberList.length;
    setCapacity(() => value);
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
      <Title>🔧스터디 관리</Title>
      <HorizontalLine></HorizontalLine>
      <StudyToggle state={recruit} setState={setRecruit}></StudyToggle>

      <MemberArea>
        <Category>🙋회원 목록</Category>
        {MemberListDoms}
      </MemberArea>
      <HorizontalLine2></HorizontalLine2>

      <FlexContainer2>
        <Company>
          <Category>지원 회사</Category>
          {company}
        </Company>
        <Job>
          <Category>지원 직무</Category>
          <div type="text" onChange={changeJob}>
            {job ? job : <div>없음</div>}
          </div>
        </Job>
        <CareerLevel>
          <Category>경력</Category>
          <div>{careerLevel}</div>
          {/* <CareerLevelInputBox value={careerLevel} onChange={changeCareerLevel}>
            <CareerLevelType></CareerLevelType>
          </CareerLevelInputBox> */}
        </CareerLevel>
      </FlexContainer2>

      <FlexContainer>
        <Capacity style={{ marginRight: "125px" }}>
          <Category>최대 인원</Category>
          <CapacityInputBox
            type="number"
            value={capacity}
            min={memberList.length}
            max={6}
            onChange={changeCapacity}
          ></CapacityInputBox>
        </Capacity>
        <DeadLine>
          <Category>마감일</Category>
          <DeadLineInputBox
            value={deadline}
            type="date"
            onChange={changeDeadline}
            min={minDate}
          ></DeadLineInputBox>
        </DeadLine>
      </FlexContainer>

      <HorizontalLine2></HorizontalLine2>
      <StudyName>
        <Category>스터디 제목</Category>
        <StudyTitleInputBox
          type="text"
          value={studyTitle}
          onChange={changeTitle}
          maxLength={100}
        ></StudyTitleInputBox>
      </StudyName>
      <Description>
        <Category>스터디 소개글</Category>
        <TextArea
          type="text"
          value={studyDesc}
          onChange={changeDesc}
        ></TextArea>
      </Description>
      <TagArea>
        <Category>태그 목록</Category>
        {TagListDoms}
      </TagArea>

      <HorizontalLine2></HorizontalLine2>
      <StudyManage>
        <FlexContainer>
          <MainButton
            width={120}
            height={35}
            marginright={10}
            content={"스터디삭제"}
            onClick={() => deleteStudy()}
          />
          <MainButton
            width={120}
            height={35}
            marginright={10}
            content={"변경사항 저장"}
            onClick={() => updateStudy()}
          />
        </FlexContainer>

        {/* <DeleteButton onClick={() => deleteStudy()}>스터디 삭제</DeleteButton> */}
        {/* <UpdateButton onClick={() => updateStudy()}>변경사항 저장</UpdateButton> */}
      </StudyManage>
    </Container>
  );
}

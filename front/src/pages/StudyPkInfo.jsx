import { styled } from "styled-components";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "./../modules/Other/Axios/customAxios";
import UserProfile from "./../components/Common/UserProfile";
import { BiCrown } from "react-icons/bi";

const Container = styled.div`
  width: 500px;
`;

const Title = styled.div`
  border: 1px solid black;

  font-size: 30px;
`;

const MemberArea = styled.div`
  border: 1px solid black;
`;

const Profile = styled.span`
  display: inline-flex;
`;

const MemberName = styled.span`
  display: inline-flex;
  align-items: center;
`;

const MemberBox = styled.div`
  display: flex;
  align-items: center;
`;

const StudyName = styled.div`
  border: 1px solid black;
`;

const TagArea = styled.div`
  border: 1px solid black;
`;

const TagBox = styled.div``;

const Description = styled.div`
  border: 1px solid black;
`;

const Category = styled.div`
  font-size: 20px;
`;

export default function StudyPkInfo() {
  const location = useLocation();

  const studyId = location.pathname.split("/")[2];

  const [memberList, setMemberList] = useState([]);
  const [tagList, setTagList] = useState([]);
  const [studyName, setStudyName] = useState("");
  const [studyDesc, setStudyDesc] = useState("");
  const [studyLeaderId, setStudyLeaderId] = useState();

  useEffect(() => {
    customAxios()
      .get(`studies/${studyId}/detail`)
      .then(({ data }) => {
        console.log(data);
        setMemberList((prev) => [...data.members]);
        setTagList((prev) => [...data.tags]);
        setStudyName((prev) => data.title);
        setStudyDesc((prev) => data.description);
        setStudyLeaderId((prev) => data.leader.member_id);
      });
  }, []);

  return (
    <Container>
      <Title>스터디 정보</Title>
      <MemberArea>
        <Category>회원 목록</Category>
        {memberList.map((member, idx) => {
          return (
            <MemberBox key={member.member_id}>
              <Profile>
                <UserProfile
                  backgroundcolor={member.background}
                  characterimg={member.character}
                ></UserProfile>
              </Profile>
              <MemberName>
                {member.nickname}
                {member.member_id === studyLeaderId ? <BiCrown /> : <></>}
              </MemberName>
            </MemberBox>
          );
        })}
      </MemberArea>
      <StudyName>
        <Category>스터디 제목</Category>
        {studyName}
      </StudyName>
      <TagArea>
        <Category>태그 목록</Category>
        {tagList.map((tag, idx) => {
          return <TagBox key={idx}>{tag}</TagBox>;
        })}
      </TagArea>
      <Description>
        <Category>스터디 설명</Category>
        {studyDesc}
      </Description>
    </Container>
  );
}

import { styled } from "styled-components";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { customAxios } from "./../modules/Other/Axios/customAxios";

const Container = styled.div``;

const Title = styled.div``;

const MemberArea = styled.div``;

const MemberBox = styled.div``;

const StudyName = styled.div``;

const TagArea = styled.div``;

const TagBox = styled.div``;

const Description = styled.div``;
export default function StudyPkAdmin() {
  const location = useLocation();

  const member = useSelector((state) => state.UserReducer);
  const memberId = member.memberId;
  const studyId = location.pathname.split("/")[2];

  const [memberList, setMemberList] = useState([]);
  const [tagList, setTagList] = useState([]);
  const [study, setStudy] = useState();
  const [studyName, setStudyName] = useState("");
  const [studyDesc, setStudyDesc] = useState("");

  useEffect(() => {
    customAxios()
      .get(`studies/${studyId}/detail`)
      .then(({ data }) => {
        console.log(data);
        setMemberList((prev) => [...data.members]);
        setTagList((prev) => [...data.tags]);
        setStudyName((prev) => data.title);
        setStudyDesc((prev) => data.description);
      });
  }, []);

  return (
    <Container>
      <Title></Title>
      <MemberArea>
        {memberList.map((member, idx) => {
          return (
            <MemberBox key={member.member_id}>{member.nickname}</MemberBox>
          );
        })}
      </MemberArea>
      <StudyName>{studyName}</StudyName>
      <TagArea>
        {tagList.map((tag, idx) => {
          return <MemberBox key={idx}>{tag}</MemberBox>;
        })}
      </TagArea>
      <Description>{studyDesc}</Description>
    </Container>
  );
}

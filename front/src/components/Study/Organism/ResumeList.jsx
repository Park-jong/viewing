// ResumeList.js

import styled from "styled-components";
import ResumeBox from "../ResumeBox";
import { useLocation } from "react-router";

const Container = styled.div`
  /* position: relative;
  display: flex;
  flex-direction: column; */
`;

export default function ResumeList({ data, setData }) {
  const study_id = useLocation().pathname.split("/")[2];

  return (
    <Container>
      {data.map((resume) => (
        <ResumeBox
          key={resume.request_id}
          study_id={study_id}
          member_id={resume.user.member_id}
          request_id={resume.request_id}
          backgroundcolor={resume.user.background}
          characterimg={resume.user.character}
          nickname={resume.user.nickname}
          date={resume.created_at}
          text={resume.content}
          request_files={resume.request_files}
          setList={setData}
        />
      ))}
    </Container>
  );
}

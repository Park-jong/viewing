// ResumeList.js

import styled from "styled-components";
import ResumeBox from "../ResumeBox";

const Container = styled.div`
  display: flex;
  flex-direction: column;
`;

export default function ResumeList() {
  const data = [
    // Sample data
    {
      request_id: 12,
      user: {
        id: 1,
        nickname: "123",
        background: "#ff6767",
        character: "cow",
      },
      content: "들어가고싶어요",
      created_at: "2023-07-16",
      request_files: [
        {
          filename: "합격인증",
          fileContent: "파일바이너리",
          fileType: "PNG",
        },
      ],
    },
    {
      request_id: 13,
      user: {
        id: 1,
        nickname: "123",
        background: "#ff6767",
        character: "cow",
      },
      content: "들어가고싶어요",
      created_at: "2023-07-16",
      request_files: [
        {
          filename: "합격인증",
          fileContent: "파일바이너리",
          fileType: "PNG",
        },
      ],
    },
    {
      request_id: 13,
      user: {
        id: 1,
        nickname: "123",
        background: "#ff6767",
        character: "cow",
      },
      content: "들어가고싶어요",
      created_at: "2023-07-16",
      request_files: [
        {
          filename: "합격인증",
          fileContent: "파일바이너리",
          fileType: "PNG",
        },
      ],
    },
  ];

  return (
    <Container>
      {data.map((resume) => (
        <ResumeBox
          key={resume.request_id}
          backgroundcolor={resume.user.background}
          characterimg={resume.user.character}
          nickname={resume.user.nickname}
          date={resume.created_at}
          text={resume.content}
          requestFiles={resume.request_files}
        />
      ))}
    </Container>
  );
}

import styled from "styled-components";
import UserProfile from "../Common/UserProfile";

const Container = styled.div`
  border: 1px solid black;
`;

const MessageContainer = styled.div`
  display: absolute;
  width: 100%;
  border: 1px solid black;
`;

export default function MeetingChatBox({ data }) {
  return (
    <Container>
      {data.sender === "notice" ? (
        "-알림-"
      ) : (
        <UserProfile
          nickname={JSON.parse(data.sender).clientData.nickname}
          backgroundcolor={JSON.parse(data.sender).clientData.backgroundColor}
          characterimg={JSON.parse(data.sender).clientData.backgroundImg}
        ></UserProfile>
      )}
      <MessageContainer>{data.message}</MessageContainer>
    </Container>
  );
}

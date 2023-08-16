import styled from "styled-components";
import UserProfile from "../Common/UserProfile";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  /* align-items: center; */
  border: 1px solid var(--gray-200);
  padding: 10px;
  gap: 10px;
`;

const MessageContainer = styled.div`
  width: 92%;
  /* border: 1px solid var(--gray-200); */
  background-color: var(--gray-200);
  padding: 10px;
  border-radius: 10px;
  font-size: 13px;
`;

const Notice = styled.div`
  font-weight: bold;
  font-size: 16px;
  color: red;
`;

export default function MeetingChatBox({ data }) {
  return (
    <Container>
      {data.sender === "notice" ? (
        <Notice>알림</Notice>
      ) : (
        <UserProfile
          nickname={JSON.parse(data.sender).clientData.nickname}
          backgroundcolor={JSON.parse(data.sender).clientData.backgroundColor}
          characterimg={JSON.parse(data.sender).clientData.backgroundImg}
          member_id={JSON.parse(data.sender).clientData.member_id}
        ></UserProfile>
      )}

      <MessageContainer>{data.message}</MessageContainer>
    </Container>
  );
}

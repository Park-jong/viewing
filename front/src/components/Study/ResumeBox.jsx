import styled from "styled-components";
import UserProfile from "../Common/UserProfile";
import MainButton from "../Button/MainButton";

const Container = styled.div`
  width: 700px;
  height: 200px;
  border-radius: 36px;
  border: 2px solid var(--gray-200);
  position: relative;

  left: 300px;
  display: flex;

  margin: 7px;
`;

const ProfileContainer = styled.div`
  position: absolute;
  left: 30px;
  top: 10px;
`;

const DateContainer = styled.div`
  position: absolute;
  right: 30px;
  top: 10px;
`;

const TextContainer = styled.div`
  width: 600px;
  height: 80px;

  padding: 10px;
  line-height: 150%;

  position: absolute;
  left: 40px;
  top: 50px;

  background-color: #f8f6ff;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  bottom: 10px;
  left: 250px;
`;

export default function ResumeBox({
  backgroundcolor,
  characterimg,
  nickname,
  date,
  text,
  requestFiles,
}) {
  return (
    <Container>
      <ProfileContainer>
        <UserProfile
          backgroundcolor={backgroundcolor}
          characterimg={characterimg}
          nickname={nickname}
        />
      </ProfileContainer>
      <DateContainer>{date}</DateContainer>
      <TextContainer>{text}</TextContainer>
      <ButtonContainer>
        <MainButton
          marginRight={10}
          width={100}
          height={30}
          fontSize={14}
          content="승인"
        />
        <MainButton
          marginRight={10}
          width={100}
          height={30}
          fontSize={14}
          content="거절"
        />
      </ButtonContainer>
    </Container>
  );
}

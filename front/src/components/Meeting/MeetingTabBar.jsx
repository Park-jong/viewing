import styled from "styled-components";
import MainButton from "../Button/MainButton";
import SubButton from "./../Button/SubButton";

const Container = styled.div`
  border: 1px solid black;
  display: flex;
  flex-direction: row;
`;

const ButtonContainer = styled.div`
  border: 1px solid black;
`;

export default function MeetingTabBar() {
  return (
    <Container>
      <ButtonContainer>현재인원</ButtonContainer>
      <ButtonContainer>채팅</ButtonContainer>
      <ButtonContainer>피드백</ButtonContainer>
    </Container>
  );
}

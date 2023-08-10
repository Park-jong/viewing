import styled from "styled-components";

const Container = styled.div`
  border: 1px solid black;
  display: flex;
  flex-direction: row;
`;

const ButtonContainer = styled.div`
  border: 1px solid black;
`;

const ExitContainer = styled.div`
  position: absolute;
  right: 0%;
  border: 1px solid black;
`;

export default function MeetingTabBar({ changeOption, toggleSideBar }) {
  return (
    <Container>
      <ButtonContainer
        onClick={() => {
          changeOption("member");
        }}
      >
        {"참여자"}
      </ButtonContainer>
      <ButtonContainer
        onClick={() => {
          changeOption("chat");
        }}
      >
        {"채팅"}
      </ButtonContainer>
      <ButtonContainer
        onClick={() => {
          changeOption("feedback");
        }}
      >
        {"피드백"}
      </ButtonContainer>
      <ExitContainer
        onClick={() => {
          toggleSideBar(true);
        }}
      >
        X
      </ExitContainer>
    </Container>
  );
}

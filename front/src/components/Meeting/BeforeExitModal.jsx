import styled from "styled-components";
const Container = styled.div`
  border: 1px solid black;
`;

const HeaderContainer = styled.div`
  border: 1px solid black;
  display: flex;
  justify-content: space-between;
`;

const MainContainer = styled.div`
  border: 1px solid black;
`;

const ButtonContainer = styled.div`
  right: 0;
  border: 1px solid black;
`;

const ContentContainer = styled.textarea`
  border: 1px solid black;
`;

const FooterContainer = styled.div`
  border: 1px solid black;
`;

export default function BeforeExitModal({
  downloadFeedback,
  copyFeedback,
  compFeedback,
  closeModal,
  leaveSession,
}) {
  return (
    <Container>
      <HeaderContainer>
        {"피드백"}
        <ButtonContainer
          onClick={() => {
            closeModal();
          }}
        >
          {"X"}
        </ButtonContainer>
      </HeaderContainer>
      <MainContainer>
        <ButtonContainer
          onClick={() => {
            copyFeedback();
          }}
        >
          {"복사"}
        </ButtonContainer>
        <ButtonContainer
          onClick={() => {
            downloadFeedback();
          }}
        >
          {"다운로드"}
        </ButtonContainer>
        <ContentContainer
          id="feedbackArea"
          placeholder="입력된 피드백이 없습니다."
          value={compFeedback()}
        ></ContentContainer>
      </MainContainer>
      <FooterContainer>
        <ButtonContainer
          onClick={() => {
            leaveSession();
          }}
        >
          {"회의 나가기"}
        </ButtonContainer>
      </FooterContainer>
    </Container>
  );
}

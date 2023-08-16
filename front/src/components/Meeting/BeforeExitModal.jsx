import styled from "styled-components";

const Container = styled.div`
  border: 1px solid #ccc;
  background-color: white;
  width: 500px;
  height: 500px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  position: relative; /* Added */
`;

const HeaderContainer = styled.div`
  background-color: #f5f5f5;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ccc;
`;

const MainContainer = styled.div`
  padding: 20px;
`;

const FlexContainer = styled.div`
  display: flex;
  justify-content: flex-end;
`;

const ButtonContainer = styled.div`
  border: 1px solid #ccc;
  background-color: #f5f5f5;
  color: #333;
  padding: 8px 16px;
  margin-right: 10px;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s ease, color 0.3s ease;

  &:hover {
    background-color: #333;
    color: white;
  }
`;

const ContentContainer = styled.textarea`
  border: 1px solid #ccc;
  width: 93%;
  padding: 10px 10px;
  margin-top: 10px;
  resize: none;
  height: 270px;
`;

const FooterContainer = styled.div`
  background-color: #f5f5f5;
  padding: 10px 0px;
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid #ccc;
  position: absolute; /* Added */
  bottom: 0; /* Added */
  width: 500px;
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
        📋 피드백
        <ButtonContainer onClick={closeModal}>X</ButtonContainer>
      </HeaderContainer>
      <MainContainer>
        <FlexContainer>
          <ButtonContainer onClick={copyFeedback}>복사</ButtonContainer>
          <ButtonContainer onClick={downloadFeedback}>다운로드</ButtonContainer>
        </FlexContainer>
        <ContentContainer
          id="feedbackArea"
          placeholder="입력된 피드백이 없습니다."
          value={compFeedback()}
          readOnly
        />
        <div style={{ marginTop: "5px", fontSize: "13px", color: "blue" }}>
          💁스터디원들이 입력한 나에 관한 피드백 사항입니다.
        </div>
      </MainContainer>
      <FooterContainer>
        <ButtonContainer onClick={leaveSession} style={{ color: "red" }}>
          회의 나가기
        </ButtonContainer>
      </FooterContainer>
    </Container>
  );
}

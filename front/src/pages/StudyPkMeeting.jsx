import MainButton from "../components/Button/MainButton";
import styled from "styled-components";
import { useLocation, useNavigate } from "react-router-dom";

const Container = styled.div`
  /* position: absolute; */
  /* top: 50%; */
  /* left: 50%; */
  /* transform: translate(-50%, -50%); */

  display: flex;
  flex-direction: column;
`;

const Title = styled.div`
  font-size: 24px;
  font-weight: 700;
  margin: 10px 0px;
`;

const SubTitle = styled.div`
  font-size: 18px;
  font-weight: 300;
  margin-bottom: 10px;
  margin-top: 20px;
`;

const Content = styled.div`
  font-size: 18px;
  font-weight: 500;
`;

const NumContainer = styled.span`
  font-family: "HSSummer";
  color: var(--primary);
`;

export default function StudyPkMeeting() {
  const studyId = useLocation().pathname.split("/")[2];

  const navigate = useNavigate();

  const goToReadyPage = () => {
    navigate(`/meeting/${studyId}/ready`);
  };

  return (
    <Container>
      <Title>💻화상 스터디에 참여하시나요?</Title>
      <br></br>
      <br></br>

      <Content>
        <NumContainer>1. </NumContainer>
        타이머 기능을 통해 시간 관리를 연습하세요.
        <br></br>
        <br></br>
        <NumContainer>2. </NumContainer>
        녹화기능을 통해 자신의 모습을 기록하세요. <br></br>
        <br></br>
        <NumContainer>3. </NumContainer>
        멤버별 피드백을 남기고, 나에 관한 피드백을 확인할 수 있어요.
      </Content>
      <br></br>

      <SubTitle>
        👇아래 버튼을 통해 뷰잉만의 특별한 영상회의 서비스를 이용해보세요!
      </SubTitle>
      <MainButton
        fontSize={28}
        height={50}
        width={200}
        onClick={goToReadyPage}
        content={"회의 입장"}
      ></MainButton>
    </Container>
  );
}

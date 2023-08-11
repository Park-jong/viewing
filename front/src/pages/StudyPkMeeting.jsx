import MainButton from "../components/Button/MainButton";
import styled from "styled-components";
import { useLocation, useNavigate } from "react-router-dom";

const Container = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

export default function StudyPkMeeting() {
  const studyId = useLocation().pathname.split("/")[2];

  const navigate = useNavigate();

  const goToReadyPage = () => {
    navigate(`/meeting/${studyId}/ready`);
  };

  return (
    <Container>
      <MainButton
        fontSize={50}
        height={300}
        width={400}
        onClick={goToReadyPage}
        content={"회의 입장"}
      ></MainButton>
    </Container>
  );
}

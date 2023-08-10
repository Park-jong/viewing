import styled from "styled-components";

const Container = styled.div`
  border: 1px solid black;
`;

export default function ExitButton({ openModal, leaveSession }) {
  return (
    <Container
      onClick={() => {
        openModal();
      }}
    >
      {"피드백 확인 및 나가기"}
    </Container>
  );
}

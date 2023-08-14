import styled from "styled-components";

const Container = styled.div`
  display: inline-block;
  padding: 10px 20px;
  background-color: #3498db;
  color: white;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #2980b9;
  }
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

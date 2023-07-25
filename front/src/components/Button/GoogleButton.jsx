import styled from "styled-components";

const Container = styled.div`
  width: 250px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: var(--gray-200);
  color: var(--gray-900);
  font-weight: 500;
  border-radius: 15px;

`;

export default function GoogleButton() {
  return <Container>Google로 시작하기</Container>
}
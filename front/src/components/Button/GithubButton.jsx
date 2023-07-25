import styled from "styled-components";

const Container = styled.div`
  width: 250px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: black;
  color: var(--gray-50);
  font-weight: 500;
  border-radius: 15px;

`;

export default function GithubButton() {
  return <Container>Github으로 시작하기</Container>;
}

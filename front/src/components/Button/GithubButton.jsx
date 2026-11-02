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

// 프론트 단독 실행: 외부 OAuth 대신 목 로그인 처리 페이지로 이동한다.
export function LoginGithub() {
  localStorage.setItem("web", "github");
  window.location.href = "/login/loading?code=mock";
}

export default function GithubButton() {
  return <Container onClick={LoginGithub}>Github으로 시작하기</Container>;
}

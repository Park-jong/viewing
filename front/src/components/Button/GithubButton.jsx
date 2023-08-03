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

export function LoginGithub() {
  const GITHUB_URL = "https://github.com/login/oauth/authorize";
  const queries = {
    client_id: "849f92ca3e7a5a3a76da",
    redirect_uri: "http://localhost:3000/login/loading",
    scope : "user"
  };

  const params = new URLSearchParams(queries).toString();
  localStorage.setItem("web", "github")
  window.location.href =`${GITHUB_URL}?${params}`;
}

export default function GithubButton() {
  return <Container onClick={LoginGithub}>Github으로 시작하기</Container>;
}

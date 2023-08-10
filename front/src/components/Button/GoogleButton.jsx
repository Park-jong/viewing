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

export function LoginGoogle() {
  const Google_URL = "https://accounts.google.com/o/oauth2/v2/auth";
  const queries = {
    response_type: "code",
    client_id:
      "249028033375-3q56vn82p2jku86es16u191kflqp6p1o.apps.googleusercontent.com",
    redirect_uri: process.env.REACT_APP_SERVER_URL + "/login/loading",
    scope: "email",
  };

  const params = new URLSearchParams(queries).toString();

  localStorage.setItem("web", "google");
  window.location.href = `${Google_URL}?${params}`;
}

export default function GoogleButton() {
  return <Container onClick={LoginGoogle}>Google로 시작하기</Container>;
}

import styled from "styled-components";

const Container = styled.div`
  width: 250px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f7e600;
  color: var(--gray-900);
  font-weight: 500;
  border-radius: 15px;
`;

// 프론트 단독 실행: 외부 OAuth 대신 목 로그인 처리 페이지로 이동한다.
export function LoginKakao() {
  localStorage.setItem("web", "kakao");
  window.location.href = "/login/loading?code=mock";
}

export default function KakaoButton() {
  return <Container onClick={LoginKakao}>Kakao로 시작하기</Container>;
}

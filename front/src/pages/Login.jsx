import { styled } from "styled-components";
import GithubButton from "../components/Button/GithubButton";
import GoogleButton from "../components/Button/GoogleButton";
import KakaoButton from "../components/Button/KakaoButton";

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 550px;
`;

const InnerContainer = styled.div``;

const H1 = styled.h1`
  text-align: center;
  font-size: 20px;
`;
const Copywrite = styled.h1`
  text-align: center;
  font-size: 40px;
  margin-top: 15px;
  margin-bottom: 30px;
`;

const ButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 15px;
`;

export default function Login() {
  return (
    <Container>
      <InnerContainer>
        <H1>로그인 / 회원가입</H1>
        <Copywrite>뷰게더에 오신 것을 환영합니다.</Copywrite>
        <ButtonsContainer>
          <KakaoButton></KakaoButton>
        </ButtonsContainer>
        <ButtonsContainer>
          <GoogleButton></GoogleButton>
        </ButtonsContainer>
        <ButtonsContainer>
          <GithubButton></GithubButton>
        </ButtonsContainer>
      </InnerContainer>
    </Container>
  );
}

import { styled } from "styled-components";
import MainButton from "../components/Button/MainButton";
import { useNavigate } from "react-router-dom";

const Container = styled.div`
  width: 100%;
  height: 100%;
`;

const TextContainer = styled.div`
  padding-top: 10%;
  padding-left: 20%;
  padding-right: 20%;
  font-size: xx-large;
  text-align: center;
`;
const ButtonContainger = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-top: 5%;
  padding-left: 20%;
  padding-right: 20%;
`;

export default function NotFound() {
  const navigate = useNavigate();

  return (
    <Container>
      <TextContainer>{"잘못된 주소입니다."}</TextContainer>
      <ButtonContainger>
        <MainButton
          content={"뒤로 가기"}
          width={300}
          height={100}
          fontSize={50}
          onClick={() => {
            navigate(-1);
          }}
        ></MainButton>
        <MainButton
          content={"홈으로 가기"}
          width={300}
          height={100}
          fontSize={50}
          onClick={() => {
            navigate("/");
          }}
        ></MainButton>
      </ButtonContainger>
    </Container>
  );
}

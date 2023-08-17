import { styled } from "styled-components";
import InputBox from "../components/Common/InputBox";
import MainButton from "../components/Button/MainButton";
import { useState } from "react";
import { useSelector } from "react-redux";
import { UserReducer } from "./../modules/UserReducer/UserReducer";
import { useLocation } from "react-router";
import { customAxios } from "../modules/Other/Axios/customAxios";

const Container = styled.div`
  padding: 20px;
`;

const H1 = styled.div`
  margin-bottom: 20px;
  font-size: 20px;
  display: flex;
  align-items: center;
`;

const TitleContainer = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 20px;
`;

const Title = styled.div`
  margin-right: 12px;
`;
const Content = styled.textarea`
  width: 355px;
  resize: none;
  height: 200px;
  margin-bottom: 20px;
`;

const ButtonsContainer = styled.div`
  width: 355px;
  display: flex;
  justify-content: center;
`;

const Nickname = styled.div`
  max-width: 200px;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
`;

export default function Message() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const author_id = useSelector((state) => state.UserReducer.memberId);
  const receiver_id = new URLSearchParams(window.location.search).get(
    "member_id"
  );
  const nickname = new URLSearchParams(window.location.search).get("nickname");
  const SERVER = process.env.REACT_APP_SERVER_URL + "messages";

  function handleContent(e) {
    setContent(e.target.value);
  }

  function handleSubmit() {
    if (!title.trim()) {
      alert("제목을 입력해주세요.");
      return;
    } else if (!content.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }
    customAxios()
      .post(SERVER, { title, content, author_id, receiver_id })
      .then((res) => {
        window.close();
      })
      .catch((err) => console.log(err));
  }

  return (
    <Container>
      <H1>
        <Nickname>{nickname}</Nickname>에게 쪽지 보내기
      </H1>
      <TitleContainer>
        <Title>제목 :</Title>
        <InputBox setText={setTitle} height={20} width={200} maxLength={100} />
      </TitleContainer>
      <Content onChange={handleContent} maxLength={1000}></Content>
      <ButtonsContainer>
        <MainButton
          onClick={() => handleSubmit()}
          content={"쪽지보내기"}
          width={100}
          height={40}
          marginright={20}
        ></MainButton>
        <MainButton
          content={"취소"}
          width={100}
          height={40}
          onClick={window.close}
        ></MainButton>
      </ButtonsContainer>
    </Container>
  );
}

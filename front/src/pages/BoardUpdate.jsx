import { styled } from "styled-components";
import MainButton from "../components/Button/MainButton";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { UserReducer } from "./../modules/UserReducer/UserReducer";
import UploadFile from "./../components/Common/UploadFile";

const Container = styled.div``;

const Category = styled.div``;

const CategorySelect = styled.select``;

const Title = styled.div``;

const TitleInput = styled.input``;

const Content = styled.div``;

const TextArea = styled.textarea``;

const ButtonsContainer = styled.div`
  display: flex;
`;

export default function BoardUpdate() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const upload_files = [];
  const navigate = useNavigate();
  const param = useLocation().pathname.split("/");
  const board_type = () => {
    if (param[2] === "free") {
      return "general";
    } else if (param[2] === "interview") {
      return "review";
    } else if (param[2] === "question") {
      return "qna";
    }
  };
  const handleTitle = (e) => {
    setTitle(e.target.value);
  };
  const handleContent = (e) => {
    setContent(e.target.value);
  };

  const SendRequest = () => {
    customAxios()
      .put(`boards/${board_type()}/${param[3]}`, {
        title,
        content,
        member_id,
        upload_files,
      })
      .then((res) => {
        navigate(`/board/${param[2]}/${param[3]}`);
      })
      .catch((err) => console.log(err));
  };

  useEffect(() => {
    customAxios()
      .get(`boards/${board_type()}/${param[3]}`)
      .then((res) => {
        setTitle(res.data.title);
        setContent(res.data.content);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <Container>
      <Title>
        게시글 제목{" "}
        <TitleInput
          defaultValue={title}
          onChange={(e) => handleTitle(e)}
        ></TitleInput>{" "}
      </Title>
      <Content>내용</Content>
      <TextArea
        defaultValue={content}
        onChange={(e) => handleContent(e)}
      ></TextArea>
      <ButtonsContainer>
        <MainButton
          width={80}
          height={35}
          marginright={10}
          content={"수정하기"}
          onClick={() => SendRequest()}
        ></MainButton>
        <MainButton width={80} height={35} content={"취소"}></MainButton>
      </ButtonsContainer>
    </Container>
  );
}

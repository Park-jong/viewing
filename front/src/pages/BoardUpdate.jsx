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
  const [files, setFiles] = useState([]);
  const [deleted, setDeleted] = useState([]);
  const navigate = useNavigate();
  const param = useLocation().pathname.split("/");
  const token = localStorage.getItem("access_token")
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
    const formData = new FormData();
    const request = {member_id, content,title, files_deleted:deleted}
    console.log(request, files)
    // formData.append("member_id", member_id);
    // formData.append("content", text);
    files.forEach((file)=> formData.append("request_files", file))
    formData.append(
      'request',
      new Blob([JSON.stringify(request)], { type: 'application/json' })
    );

    customAxios()
      .put(`boards/${board_type()}/${param[3]}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: "Bearer " + token,
        },
      })
      .then(function (res) {
        console.log(res);
        navigate(`/board/${param[2]}/${param[3]}`)
      })
      .catch((error) => {
        console.error("에러가 발생했습니다.:", error);
      });
  };

  useEffect(() => {
    customAxios()
      .get(`boards/${board_type()}/${param[3]}`)
      .then((res) => {
        setTitle(res.data.title);
        setContent(res.data.content);
        setFiles(res.data.article_files)
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
      <UploadFile width={200} height={100} files={files} setFiles={setFiles} deleted={deleted} setDeleted={setDeleted}></UploadFile>
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

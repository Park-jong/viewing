import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { styled } from "styled-components";
import { customAxios } from "../modules/Other/Axios/customAxios";
import UploadFile from "../components/Common/UploadFile";
import MainButton from "../components/Button/MainButton";

const Container = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
`;

const Title = styled.div`
  margin-bottom: 10px;
`;

const TitleInput = styled.input`
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;

  color: black;
  font-size: 13px;
`;

const Content = styled.div`
  margin-bottom: 10px;
`;

const TextArea = styled.textarea`
  width: 100%;
  height: 500px;

  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-family: "Pretendard";

  color: black;
  font-size: 15px;

  line-height: 150%;
  resize: none;

  ::placeholder {
    font-family: "Pretendard";
    color: #ccc; /* Change to your preferred placeholder color */
  }
`;

const ButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 20px;
`;

export default function StudyPkBoardUpdate() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const [files, setFiles] = useState([]);
  const [deleted, setDeleted] = useState([]);
  const navigate = useNavigate();
  const param = useLocation().pathname.split("/");
  const token = localStorage.getItem("access_token");
  const handleTitle = (e) => {
    setTitle(e.target.value);
  };
  const handleContent = (e) => {
    setContent(e.target.value);
  };

  const SendRequest = () => {
    const formData = new FormData();
    const request = { member_id, content, title, files_deleted: deleted };
    // formData.append("member_id", member_id);
    // formData.append("content", text);
    files.forEach((file) => formData.append("request_files", file));
    formData.append(
      "request",
      new Blob([JSON.stringify(request)], { type: "application/json" })
    );

    customAxios()
      .put(`studies/${param[2]}/boards/${param[4]}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: "Bearer " + token,
        },
      })
      .then((res) => {
        navigate(`/study/${param[2]}/board/${param[4]}`);
      })
      .catch((error) => {
        console.error("에러가 발생했습니다.:", error);
      });
  };

  useEffect(() => {
    customAxios()
      .get(`studies/${param[2]}/boards/${param[4]}`)
      .then((res) => {
        setTitle(res.data.title);
        setContent(res.data.content);
        setFiles(res.data.article_files);
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
          maxLength={100}
        ></TitleInput>{" "}
      </Title>
      <Content>내용</Content>
      <TextArea
        defaultValue={content}
        onChange={(e) => handleContent(e)}
        maxLength={5000}
      ></TextArea>
      <UploadFile
        width={200}
        height={100}
        files={files}
        setFiles={setFiles}
        deleted={deleted}
        setDeleted={setDeleted}
      ></UploadFile>
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

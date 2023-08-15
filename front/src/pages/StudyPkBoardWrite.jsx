import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { styled } from "styled-components";
import { customAxios } from "../modules/Other/Axios/customAxios";
import UploadFile from "../components/Common/UploadFile";
import MainButton from "../components/Button/MainButton";
import { UserReducer } from "./../modules/UserReducer/UserReducer";

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

export default function StudyPkBoardWrite() {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [files, setFiles] = useState([]);
  const token = localStorage.getItem("access_token");
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const navigate = useNavigate();
  const study_id = useLocation().pathname.split("/")[2];

  const handleTitle = (e) => {
    setTitle(e.target.value);
  };
  const handleContent = (e) => {
    setContent(e.target.value);
  };

  const SendRequest = (e) => {
    const formData = new FormData();
    const request = { member_id, content, title };
    // formData.append("member_id", member_id);
    // formData.append("content", text);
    files.forEach((file) => formData.append("request_files", file));

    formData.append(
      "request",
      new Blob([JSON.stringify(request)], { type: "application/json" })
    );

    customAxios()
      .post(`studies/${study_id}/boards`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          "Authorization": "Bearer " + token,
        },
      })
      .then(function (res) {
        console.log(res);
        navigate(`/study/${study_id}/board/${res.data}`);
      })
      .catch((error) => {
        console.error("에러가 발생했습니다.:", error);
      });
  };

  // useEffect(() => {
  //   setCategory(board_type());
  // }, []);
  return (
    <>
      <Container>
        <Title>
          <TitleInput
            placeholder="제목을 입력해주세요."
            onChange={(e) => handleTitle(e)}
            maxLength={100}
          ></TitleInput>{" "}
        </Title>
        {/* <Content>내용</Content> */}
        <TextArea
          placeholder="내용을 입력해주세요."
          onChange={(e) => handleContent(e)}
        ></TextArea>
        <UploadFile
          width={200}
          height={100}
          files={files}
          setFiles={setFiles}
        ></UploadFile>
        <ButtonsContainer>
          <MainButton
            width={80}
            height={35}
            marginright={10}
            content={"작성하기"}
            onClick={() => SendRequest()}
          ></MainButton>
          <MainButton width={80} height={35} content={"취소"}></MainButton>
        </ButtonsContainer>
      </Container>
    </>
  );
}

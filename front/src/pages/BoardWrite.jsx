import { styled } from "styled-components";
import MainButton from "../components/Button/MainButton";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { UserReducer } from "./../modules/UserReducer/UserReducer";
import UploadFile from "./../components/Common/UploadFile";

const Container = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
`;

const Category = styled.div`
  margin-bottom: 10px;
`;

const CategorySelect = styled.select`
  width: 102%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
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

export default function BoardWrite() {
  const maxLen = 5;
  
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [files, setFiles] = useState([]);
  const param = new URLSearchParams(window.location.search).get("type");
  const token = localStorage.getItem("access_token");
  const board_type = () => {
    if (param === "free") {
      return "general";
    } else if (param === "interview") {
      return "review";
    } else if (param === "question") {
      return "qna";
    }
  };
  const [category, setCategory] = useState(board_type());
  console.log(category)
  const member_id = useSelector((state) => state.UserReducer.memberId);
  const navigate = useNavigate();

  useEffect(() => {
    customAxios()
      .get(`members/${member_id}`)
      .catch((err) => {
        window.alert("로그인이 필요합니다.");
        navigate(`/board/${param}`);
      });
  }, []);

  const handleCategory = (e) => {
    setCategory(e.target.value);
  };
  const handleTitle = (e) => {
    setTitle(e.target.value);
  };
  const handleContent = (e) => {
    setContent(e.target.value);
  };

  const SendRequest = (e) => {
    
    if (!title.trim()) {
      alert("제목을 입력하세요.");
      return;
    } else if (!content.trim()) {
      alert("내용을 입력하세요.");
      return;
    }

    const formData = new FormData();
    const request = { member_id, content, title, board_type: category };
    // formData.append("member_id", member_id);
    // formData.append("content", text);
    files.forEach((file) => formData.append("request_files", file));

    formData.append(
      "request",
      new Blob([JSON.stringify(request)], { type: "application/json" })
    );
    
    console.log(request, category);
    customAxios()
      .post(`boards/${category}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: "Bearer " + token,
        },
      })
      .then(function (res) {
        navigate(`/board/${param}/${res.data}`);
      })
      .catch((error) => {
        console.error("에러가 발생했습니다.:", error);
      });
  };

  return (
    <Container>
      <Category>
        {/* 카테고리{" "} */}
        <CategorySelect
          onChange={(e) => {
            handleCategory(e);
          }}
        >
          {param === "free" ? (
            <option value={"general"} selected>
              자유게시판
            </option>
          ) : (
            <option value={"general"}>자유게시판</option>
          )}
          {param === "question" ? (
            <option value={"qna"} selected>
              질문게시판
            </option>
          ) : (
            <option value={"qna"}>질문게시판</option>
          )}
          {param === "interview" ? (
            <option value={"review"} selected>
              면접게시판
            </option>
          ) : (
            <option value={"review"}>면접 후기</option>
          )}
        </CategorySelect>
      </Category>
      <Title>
        <TitleInput
          onChange={(e) => handleTitle(e)}
          placeholder="제목을 입력해주세요."
          maxLength={100}
        ></TitleInput>{" "}
      </Title>
      {/* <Content>내용</Content> */}
      <TextArea
        placeholder=" 내용을 입력해주세요.
        내용은 최대 5000자까지 입력 가능합니다."
        onChange={(e) => handleContent(e)}
        maxLength={5000}
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
  );
}

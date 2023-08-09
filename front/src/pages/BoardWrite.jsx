import { styled } from "styled-components";
import MainButton from "../components/Button/MainButton";
import { useEffect, useState } from "react";
import { customAxios } from "../modules/Other/Axios/customAxios";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { UserReducer } from './../modules/UserReducer/UserReducer';
import UploadFile from './../components/Common/UploadFile';

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

export default function BoardWrite() {
  const [category, setCategory] = useState("");
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [files, setFiles] = useState([])
  const param = new URLSearchParams(window.location.search).get("type")
  const token = localStorage.getItem("access_token");
  const board_type = () => {
    if (param === "free") {
      return "general"
    } else if (param === "interview") {
      return "review"
    } else if (param === "question") {
      return "qna"
    }
  }
  const member_id = useSelector(state => state.UserReducer.memberId)
  const navigate = useNavigate()


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
    const formData = new FormData();
    const request = {member_id, content,title, category}
    // formData.append("member_id", member_id);
    // formData.append("content", text);
    files.forEach((file)=> formData.append("request_files", file))

    formData.append(
      'request',
      new Blob([JSON.stringify(request)], { type: 'application/json' })
    );

    customAxios()
      .post(`boards/${category}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: "Bearer " + token,
        },
      })
      .then(function (res) {
        console.log(res);
        navigate(`/board/${param}/${res.data}`)
      })
      .catch((error) => {
        console.error("에러가 발생했습니다.:", error);
      });
  }

  useEffect(()=>{
    setCategory(board_type())
  },[])

  return (
    <Container>
      <Category>
        카테고리{" "}
        <CategorySelect onChange={(e) => handleCategory(e)}>
          { param === "free" ? <option value={"general"} selected>자유게시판</option> : <option value={"general"}>자유게시판</option> }
          { param === "question" ? <option value={"qna"} selected>질문게시판</option> : <option value={"qna"}>질문게시판</option> }
          { param === "interview" ? <option value={"review"} selected>면접게시판</option> : <option value={"reivew"}>면접게시판</option> }
        </CategorySelect>
      </Category>
      <Title>
        게시글 제목 <TitleInput onChange={(e) => handleTitle(e)}></TitleInput>{" "}
      </Title>
      <Content>내용</Content>
      <TextArea onChange={(e) => handleContent(e)}></TextArea>
      <UploadFile width={200} height={100} files={files} setFiles={setFiles}></UploadFile>
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
